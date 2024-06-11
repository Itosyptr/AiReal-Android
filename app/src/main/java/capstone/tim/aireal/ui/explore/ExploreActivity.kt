package capstone.tim.aireal.ui.explore

import android.content.Context
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
import capstone.tim.aireal.databinding.ActivityExploreBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.ui.home.ProductsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ExploreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExploreBinding
    private lateinit var viewModel: ExploreViewModel
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val category = intent.getStringExtra(category).toString()
        binding.resultSearch.text = category

        pref = UserPreference.getInstance(this.dataStore)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[ExploreViewModel::class.java]

        lifecycleScope.launch {
            val token = withContext(Dispatchers.IO) {
                viewModel.getToken()
            }

            val bearerToken = "Bearer $token"
            viewModel.getProducts(bearerToken, category)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        lifecycleScope.launch {
            viewModel.listData.observe(this@ExploreActivity) { listData ->
                Log.d("getDetail", "onCreateView: $listData")

                val listDetailProduct: MutableList<DataItem?> = mutableListOf()

                viewModel.listProducts.observe(this@ExploreActivity) { listUser ->
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

        viewModel.isError.observe(this) {
            showToastError(it)
        }

        val layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.layoutManager = layoutManager

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setProductsData(listProducts: List<DataItem?>?) {
        val adapter = ProductsAdapter(this)
        adapter.submitList(listProducts)
        binding.rvProducts.adapter = adapter
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
        const val category = "category"
    }
}