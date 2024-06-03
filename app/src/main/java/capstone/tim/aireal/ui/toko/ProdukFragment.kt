package capstone.tim.aireal.ui.toko

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentTokoBinding
import capstone.tim.aireal.ui.toko.TokoAdapter

class ProdukFragment : Fragment(R.layout.fragment_tokoku) {

    private var _binding: FragmentTokoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProdukViewModel
    private lateinit var adapter: TokoAdapter
    private lateinit var tokoId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTokoBinding.bind(view)

        adapter = TokoAdapter()
        adapter.notifyDataSetChanged()
        binding.apply {
            rvToko.setHasFixedSize(true)
            rvToko.layoutManager = LinearLayoutManager(activity)
            rvToko.adapter = adapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ProdukViewModel::class.java)
        viewModel.setListProduk(tokoId) // You should set tokoId accordingly
        viewModel.listProduk.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
