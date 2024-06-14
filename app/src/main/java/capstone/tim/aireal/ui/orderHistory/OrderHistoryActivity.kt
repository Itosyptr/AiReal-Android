package capstone.tim.aireal.ui.orderHistory

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivityOrderHistoryBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.ui.keranjang.KeranjangAdapter
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("DEPRECATION")
class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryBinding
    private lateinit var viewModel: OrderHistoryViewModel
    private lateinit var pref: UserPreference
    private var bearerToken = ""
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.imageView3.setOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvProductHistory.layoutManager = layoutManager

        pref = UserPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[OrderHistoryViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            userId = user?.userId.toString()
            bearerToken = "Bearer ${user?.token.toString()}"

            viewModel.getOrderByUserId(bearerToken, userId)
        }

        lifecycleScope.launch {
            viewModel.listData.observe(this@OrderHistoryActivity) { listData ->
                val listDetailOrder: MutableList<DataItem?> = mutableListOf()

                viewModel.listProducts.observe(this@OrderHistoryActivity) { listProducts ->
                    if (listData != null && listProducts != null && listData.size == listProducts.size) {
                        var i = 0

                        for (item in listData) {
                            listDetailOrder.add(
                                DataItem(
                                    id = listProducts[i]?.productId,
                                    stock = listProducts[i]?.quantity.toString(),
                                    imageUrl = listData[i]?.imageUrl,
                                    name = listData[i]?.name,
                                    price = listData[i]?.price
                                )
                            )
                            i++
                        }

                        listData.clear()
                        setCartData(listDetailOrder)
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

    private fun setCartData(listProducts: List<DataItem?>?) {
        val adapter = KeranjangAdapter(this)
        adapter.submitList(listProducts)
        binding.rvProductHistory.adapter = adapter
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
}