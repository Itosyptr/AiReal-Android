package capstone.tim.aireal.ui.keranjang

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.FragmentKeranjangBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.dataOrder
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class keranjangFragment : Fragment() {
    private lateinit var _binding: FragmentKeranjangBinding
    private val binding get() = _binding

    private lateinit var pref: UserPreference
    private lateinit var viewModel: KeranjangViewModel

    private var userId: String = ""
    private var token: String = ""
    private var totalPrice: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        val root: View = binding.root

        pref = UserPreference.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, requireContext())
        )[KeranjangViewModel::class.java]

        setHasOptionsMenu(false)

        val layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.layoutManager = layoutManager

        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            userId = user?.userId.toString()
            token = user?.token.toString()

            val bearerToken = "Bearer $token"
            viewModel.getCart(bearerToken, userId)
        }

        lifecycleScope.launch {
            viewModel.listData.observe(viewLifecycleOwner) { listData ->
                val listDetailCart: MutableList<DataItem?> = mutableListOf()

                listDetailCart.clear()

                viewModel.listProducts.observe(viewLifecycleOwner) { listProducts ->
                    if (listData != null && listProducts != null && listData.size == listProducts.size) {
                        var i = 0

                        for (item in listData) {
                            listDetailCart.add(
                                DataItem(
                                    id = listProducts[i]?.productId,
                                    stock = listProducts[i]?.quantity.toString(),
                                    imageUrl = listData[i]?.imageUrl,
                                    name = listData[i]?.name,
                                    price = listData[i]?.price
                                )
                            )
                            totalPrice += ((listData[i].price?.toInt()!!) * (listProducts[i]?.quantity!!))
                            i++
                        }

                        binding.recyclerView.visibility = View.VISIBLE
                        binding.noDataFound.visibility = View.GONE

                        binding.tvTotalPrice.text =
                            getString(R.string.product_price, totalPrice.toString())
                        listData.clear()
                        setCartData(listDetailCart)
                        showLoading(false)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            showToastError(it)
            showLoading(false)
            binding.recyclerView.visibility = View.GONE
            binding.noDataFound.visibility = View.VISIBLE
        }

        binding.btnCheckout.setOnClickListener {
            val bearerToken = "Bearer $token"
            viewModel.orderCart(bearerToken, dataOrder(userId))

            viewModel.isLoading.observe(viewLifecycleOwner) {
                if (it == false) {
                    totalPrice = 0
                    binding.tvTotalPrice.text =
                        getString(R.string.product_price, totalPrice.toString())
                    customToast("Checkout successfully")
                    binding.recyclerView.visibility = View.GONE
                    binding.noDataFound.visibility = View.VISIBLE
                }
            }
        }

        return root
    }

    private fun setCartData(listProducts: List<DataItem?>?) {
        val adapter = KeranjangAdapter(requireContext())
        adapter.submitList(listProducts)
        binding.recyclerView.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToastError(isError: Boolean) {
        if (isError) Toast.makeText(
            this.context,
            "Terjadi kesalahan!! Mohon Bersabar",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun customToast(text: String) {
        val customToastLayout = layoutInflater.inflate(R.layout.custom_toast_success,null)
        val customToast = Toast(this.context)
        customToast.view = customToastLayout
        customToastLayout.findViewById<TextView>(R.id.message_toast).text = text
        customToast.setGravity(Gravity.CENTER,0,0)
        customToast.duration = Toast.LENGTH_LONG
        customToast.show()
    }
}