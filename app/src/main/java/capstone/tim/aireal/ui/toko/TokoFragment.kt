package capstone.tim.aireal.ui.toko

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.FragmentTokoBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.ShopData
import capstone.tim.aireal.ui.addProduct.AddProductActivity
import capstone.tim.aireal.ui.editShop.EditShopActivity
import capstone.tim.aireal.ui.home.ProductsAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TokoFragment : Fragment() {

    private lateinit var binding: FragmentTokoBinding

    private lateinit var viewModel: TokoViewModel
    private lateinit var pref: UserPreference

    private var isDialogShowing = false

    private var userId: String = ""
    private var token: String = ""
    private var shopId: String = ""
    private var data: ShopData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentTokoBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(this.context, 2)
        binding.rvShopProduct.layoutManager = layoutManager

        pref = UserPreference.getInstance(requireActivity().dataStore)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(pref, requireContext())
        )[TokoViewModel::class.java]

        viewModel.getUser().observe(viewLifecycleOwner) { shop ->
            userId = shop.userId
            token = "Bearer ${shop.token}"
            viewModel.getDetailShop(token, userId)
        }

        viewModel.shopDetail.observe(viewLifecycleOwner) { shop ->
            if (shop?.id != null) {
                shopId = shop?.id.toString()
                binding.tvShopName.text = shop?.name
                Glide.with(binding.root)
                    .load(shop?.imageUrl?.get(0))
                    .into(binding.shopImage)

                data = shop.let { it1 ->
                    ShopData(
                        it1.createdAt,
                        it1.province,
                        it1.city,
                        it1.street,
                        it1.imageUrl,
                        it1.name,
                        it1.description,
                        it1.id,
                        it1.userId,
                        it1.updatedAt
                    )
                }
            }
        }

        lifecycleScope.launch {
            delay(1000L)
            viewModel.listData.observe(viewLifecycleOwner) { listData ->
                Log.d("getDetail", "onCreateView: $listData")

                val listDetailProduct: MutableList<DataItem?> = mutableListOf()

                viewModel.listProducts.observe(viewLifecycleOwner) { listUser ->
                    if (listUser != null && listData != null && listData.size == listUser.size) {
                        var i = 0

                        listDetailProduct.clear()

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

                        binding.rvShopProduct.visibility = View.VISIBLE
                        binding.noDataFound.visibility = View.GONE

                        listData.clear()
                        setProductsData(listDetailProduct)
                        showLoading(false)
                    }
                }
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            showToastError(it)
            showConfirmationDialog()
        }

        viewModel.isError2.observe(viewLifecycleOwner) {
            showLoading(false)
            binding.rvShopProduct.visibility = View.GONE
            binding.noDataFound.visibility = View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.btnAddProduct.setOnClickListener {
            if (shopId == "") {
                showConfirmationDialog()
                return@setOnClickListener
            } else {
                val intent = Intent(this.context, AddProductActivity::class.java)
                intent.putExtra(AddProductActivity.SHOP_ID, shopId)
                startActivity(intent)
            }
        }

        binding.editShop.setOnClickListener {
            if (shopId == "") {
                showConfirmationDialog()
                return@setOnClickListener
            } else {
                val intent = Intent(this.context, EditShopActivity::class.java)
                intent.putExtra(EditShopActivity.DETAIL_SHOP, data)
                intent.putExtra(EditShopActivity.TYPE, 0)
                startActivity(intent)
            }
        }

        return binding.root
    }

    private fun setProductsData(listProducts: List<DataItem?>?) {
        val adapter = ProductsAdapter(requireContext())
        adapter.submitList(listProducts)
        binding.rvShopProduct.adapter = adapter
    }

    private fun showConfirmationDialog() {
        if (!isAdded || isDialogShowing) return

        isDialogShowing = true

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.custom_dialog, null)

        builder.setView(view)

        val dialogMessage = view.findViewById<TextView>(R.id.dialog_message)
        dialogMessage.text = getString(R.string.no_shop)
        
        builder.setPositiveButton(R.string.yes) { _, _ ->
            isDialogShowing = false
            val intent = Intent(this.context, EditShopActivity::class.java)
            intent.putExtra(EditShopActivity.TYPE, 1)
            startActivity(intent)
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            isDialogShowing = false
            dialog.dismiss()

            if (isAdded) {
                parentFragmentManager.popBackStack()
            }
        }
        builder.setOnDismissListener {
            isDialogShowing = false
        }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun showToastError(isError: Boolean) {
        if (isError) Toast.makeText(
            this.context,
            "Terjadi kesalahan!! Mohon Bersabar",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}