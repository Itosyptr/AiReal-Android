package capstone.tim.aireal.ui.detailProduct

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivityDetailProductBinding
import capstone.tim.aireal.response.CartRequest
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.ItemsItem
import capstone.tim.aireal.response.ShopData
import capstone.tim.aireal.ui.shopDisplay.ShopDisplayActivity
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var viewModel: DetailProductViewModel
    private lateinit var pref: UserPreference
    private var bearerToken = ""
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val productItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(LIST_PRODUCT, DataItem::class.java)
        } else {
            intent.getParcelableExtra(LIST_PRODUCT)
        }

        val shopId = productItem?.shopId

        pref = UserPreference.getInstance(this.dataStore)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, this)
        )[DetailProductViewModel::class.java]

        lifecycleScope.launch {
            val token = withContext(Dispatchers.IO) {
                viewModel.getToken()
            }

            bearerToken = "Bearer $token"
            viewModel.getDetailShop(bearerToken, shopId!!)
        }

        getDetailProduct(productItem!!)

        viewModel.getUser().observe(this) { user ->
            userId = user?.userId.toString()
        }

        binding.apply {
            backButton.setOnClickListener {
                finish()
            }

            shareButton.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out this great product: ${productItem.name} on Aireal!"
                )
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share To:"))
            }

            cart.setOnClickListener {
                viewModel.addToCart(
                    bearerToken,
                    CartRequest(userId, listOf(ItemsItem(1, productItem.id)))
                )
                Toast.makeText(this@DetailProductActivity, "Added to cart", Toast.LENGTH_SHORT)
                    .show()
            }

            buyNow.setOnClickListener {
                Toast.makeText(
                    this@DetailProductActivity,
                    "this feature is not yet available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.shopDetail.observe(this) { detail ->
            val shopData = ShopData(
                id = detail?.id!!,
                name = detail?.name!!,
                city = detail?.city!!,
                imageUrl = detail?.imageUrl!!,
                province = detail?.province!!,
                street = detail?.street!!
            )

            binding.apply {
                shopName.text = detail?.name
                shopLocation.text = detail?.city
                Glide.with(binding.root)
                    .load(detail?.imageUrl.get(0))
                    .into(shopImage)

                visitStore.setOnClickListener {
                    val intent = Intent(this@DetailProductActivity, ShopDisplayActivity::class.java)
                    intent.putExtra(ShopDisplayActivity.EXTRA_DATA, shopData)
                    startActivity(intent)
                }
            }
        }

        viewModel.isError.observe(this) {
            showToastError(it)
        }
    }

    private fun getDetailProduct(detail: DataItem) {
        binding.apply {
            productName.text = detail.name
            productPrice.text = getString(R.string.product_price, detail.price)
            productDescription.text = detail.longdescription

            val sliderDataArrayList = ArrayList<SliderData>()
            val sliderView = findViewById<SliderView>(R.id.slider)

            for (i in detail.imageUrl!!) {
                sliderDataArrayList.add(SliderData(i))
            }

            val adapter = SliderAdapter(this@DetailProductActivity, sliderDataArrayList)

            sliderView.setSliderAdapter(adapter)
        }
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
        const val LIST_PRODUCT = "list_product"
    }
}