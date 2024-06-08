package capstone.tim.aireal.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentHomeBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.ProductsResponse

class homeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    private lateinit var rvCategories: RecyclerView
    private val list = ArrayList<Categories>()

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setHasOptionsMenu(true)

        val layoutManager = GridLayoutManager(this.context, 2)
        binding.rvProducts.layoutManager = layoutManager

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.listProducts.observe(viewLifecycleOwner) { listUser ->
            setProductsData(listUser)
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            showToastError(it)
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

        listCategoryAdapter.setOnItemClickCallback(object : ProductCategoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Categories) {
                val item = Categories(data.photo, data.name)
                Toast.makeText(context, item.name, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setProductsData(listProducts: List<DataItem?>?) {
        val adapter = ProductsAdapter()
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