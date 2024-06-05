package capstone.tim.aireal.ui.toko

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentTokokuBinding

class PenjualanFragment : Fragment(R.layout.fragment_tokoku) {
    private var _binding: FragmentTokokuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PenjualanViewModel
    private lateinit var adapter: TokoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTokokuBinding.bind(view)

        adapter = TokoAdapter(
            products = emptyList(),
            onItemClickListener = object : TokoAdapter.OnItemClickListener {
                override fun onItemClick(product: PenjualanViewModel.Penjualan) {
                    // Handle item click
                }

                override fun onItemLongClick(product: PenjualanViewModel.Penjualan) {
                    // Handle item long click
                }
            }
        )

        binding.apply {
            rvProduk.setHasFixedSize(true)
            rvProduk.layoutManager = LinearLayoutManager(activity)
            rvProduk.adapter = adapter
        }

        viewModel = ViewModelProvider(this)[PenjualanViewModel::class.java]
        viewModel.setListPenjualan()
        viewModel.listPenjualan.observe(viewLifecycleOwner) { products ->
            products?.let {
                adapter.updateProducts(it)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
