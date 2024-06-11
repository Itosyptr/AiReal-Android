package capstone.tim.aireal.ui.home

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.FragmentHomeBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.ui.explore.ExploreActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class homeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    private lateinit var pref: UserPreference

    private lateinit var rvCategories: RecyclerView
    private val list = ArrayList<Categories>()

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        pref = UserPreference.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, requireContext())
        )[HomeViewModel::class.java]

        lifecycleScope.launch {
            val token = withContext(Dispatchers.IO) {
                viewModel.getToken()
            }

            val bearerToken = "Bearer $token"
            viewModel.getProducts(bearerToken)
        }

        setHasOptionsMenu(true)

        val layoutManager = GridLayoutManager(this.context, 2)
        binding.rvProducts.layoutManager = layoutManager

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        lifecycleScope.launch {
            viewModel.listData.observe(viewLifecycleOwner) { listData ->
                Log.d("getDetail", "onCreateView: $listData")

                val listDetailProduct: MutableList<DataItem?> = mutableListOf()

                viewModel.listProducts.observe(viewLifecycleOwner) { listUser ->
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

        viewModel.isError.observe(viewLifecycleOwner) {
            showToastError(it)
        }

        binding.view4.setOnClickListener {
            val intent = Intent(context, ExploreActivity::class.java)
            startActivity(intent)
        }

        rvCategories = binding.rvCategory
        rvCategories.setHasFixedSize(true)
        list.addAll(getListCategories())
        showRecyclerList()

        return root
    }

    private fun getListCategories(): ArrayList<Categories> {
        val dataName = resources.getStringArray(R.array.product_categories)
        val dataPhoto = resources.obtainTypedArray(R.array.image_categories)
        val listCategory = ArrayList<Categories>()
        for (i in dataName.indices) {
            val category = Categories(dataPhoto.getResourceId(i, -1), dataName[i])
            listCategory.add(category)
        }
        return listCategory
    }

    private fun showRecyclerList() {
        rvCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val listCategoryAdapter = ProductCategoriesAdapter(list)
        rvCategories.adapter = listCategoryAdapter

        listCategoryAdapter.setOnItemClickCallback(object :
            ProductCategoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Categories) {
                val intent = Intent(context, ExploreActivity::class.java)
                intent.putExtra(ExploreActivity.category, data.name)
                startActivity(intent)
            }
        })
    }

    private fun setProductsData(listProducts: List<DataItem?>?) {
        val adapter = ProductsAdapter(requireContext())
        adapter.submitList(listProducts)
        binding.rvProducts.adapter = adapter
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
}