package capstone.tim.aireal.ui.toko

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.FragmentTokoBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TokoFragment : Fragment() {

    private lateinit var binding: FragmentTokoBinding

    private lateinit var viewModel: TokoViewModel
    private lateinit var pref: UserPreference

    private var userId: String = ""
    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentTokoBinding.inflate(inflater, container, false)

        pref = UserPreference.getInstance(requireActivity().dataStore)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(pref, requireContext())
        )[TokoViewModel::class.java]

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel.getUser().observe(requireActivity()) { shop ->
            userId = shop.userId
            token = "Bearer ${shop.token}"
            viewModel.getDetailShop(token, userId)
        }

        viewModel.shopDetail.observe(requireActivity()) { shop ->
            binding.tvShopName.text = shop?.name
            Glide.with(binding.root)
                .load(shop?.imageUrl?.get(0))
                .into(binding.shopImage)
        }

        viewModel.isError.observe(requireActivity()) {
            showToastError(it)
        }

        return binding.root
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Shop not found, do you want to create new Shop?")
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.back) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showToastError(isError: Boolean) {
        if (isError) Toast.makeText(
            requireContext(),
            "Terjadi kesalahan!! Mohon Bersabar",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}