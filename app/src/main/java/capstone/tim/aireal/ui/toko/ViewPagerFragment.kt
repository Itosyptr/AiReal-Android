package capstone.tim.aireal.ui.toko

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.FragmentViewPagerBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.ui.home.ProductsAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var viewModel: TokoViewModel
    private lateinit var pref: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        pref = UserPreference.getInstance(requireActivity().dataStore)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(pref, requireContext())
        )[TokoViewModel::class.java]

        val layoutManager = GridLayoutManager(this.context, 2)
        binding.rvShopProduct.layoutManager = layoutManager

        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }

        if (position == 1) {
            lifecycleScope.launch {
                delay(1000L)
                viewModel.listData.observe(requireActivity()) { listData ->
                    Log.d("getDetail", "onCreateView: $listData")

                    val listDetailProduct: MutableList<DataItem?> = mutableListOf()

                    viewModel.listProducts.observe(requireActivity()) { listUser ->
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
        }

        viewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        viewModel.isError.observe(requireActivity()) {
            showToastError(it)
        }

        return binding.root
    }

    private fun setProductsData(listProducts: List<DataItem?>?) {
        val adapter = ProductsAdapter(requireContext())
        adapter.submitList(listProducts)
        binding.rvShopProduct.adapter = adapter
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

    companion object {
        const val ARG_POSITION = "position"

        var position = 0
    }
}