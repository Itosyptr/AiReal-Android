package capstone.tim.aireal.ui.shopDisplay

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivityShopDisplayBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.ShopData
import capstone.tim.aireal.ui.home.ProductsAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ShopDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopDisplayBinding
    private lateinit var viewModel: ShopDisplayViewModel
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        pref = UserPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[ShopDisplayViewModel::class.java]

        val shopInformation = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, ShopData::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_DATA)
        }

        lifecycleScope.launch {
            val token = withContext(Dispatchers.IO) {
                viewModel.getToken()
            }

            val bearerToken = "Bearer $token"
            viewModel.getProductsbyShopId(bearerToken, shopInformation?.id.toString())
        }

        val layoutManager = GridLayoutManager(this, 2)
        binding.rvShopProducts.layoutManager = layoutManager

        binding.apply {
            shopName.text = shopInformation?.name
            shopLocation.text =
                shopInformation?.street + ", " + shopInformation?.city + ", " + shopInformation?.province
            Glide.with(binding.root)
                .load(shopInformation?.imageUrl?.get(0))
                .into(shopImage)

            imageView5.setOnClickListener {
                finish()
            }

            shareButton.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out this great Shop: ${shopInformation?.name} on Aireal!"
                )
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share To:"))
            }
        }

        lifecycleScope.launch {
            viewModel.listData.observe(this@ShopDisplayActivity) { listData ->
                Log.d("getDetail", "onCreateView: $listData")

                val listDetailProduct: MutableList<DataItem?> = mutableListOf()

                viewModel.listProducts.observe(this@ShopDisplayActivity) { listUser ->
                    if (listUser != null && listData != null && listData.size == listUser.size) {
                        var i = 0

                        for (item in listUser) {
                            listDetailProduct.add(
                                DataItem(
                                    price = item?.price,
                                    imageUrl = item?.imageUrl,
                                    longdescription = item?.longdescription,
                                    name = item?.name,
                                    id = item?.id,
                                    shopId = item?.shopId,
                                    stock = item?.stock,
                                    categoryId = item?.categoryId,
                                    description = item?.description,
                                    createdAt = item?.createdAt,
                                    updatedAt = item?.updatedAt,
                                    location = listData[i]
                                )
                            )
                            i++
                        }

                        listData.clear()
                        setProductsData(listDetailProduct)
                        showLoading(false)
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.isError.observe(this) {
            showToastError(it)
        }
    }

    private fun setProductsData(listProducts: List<DataItem?>?) {
        val adapter = ProductsAdapter(this)
        adapter.submitList(listProducts)
        binding.rvShopProducts.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToastError(isError: Boolean) {
        if (isError) Toast.makeText(
            this,
            "Terjadi kesalahan!! Mohon Bersabar",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}