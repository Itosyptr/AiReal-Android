package capstone.tim.aireal.ui.toko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentTokoBinding

class TokoFragment : Fragment() {

    private var _binding: FragmentTokoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TokoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTokoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TokoViewModel::class.java]

        val viewPager = binding.viewPager

        viewPager.adapter = SectionPagerAdapter(this)

        viewModel.profileImage.observe(viewLifecycleOwner) { imageResId ->
            binding.profileImageView.setImageResource(imageResId)
        }

        viewModel.profileName.observe(viewLifecycleOwner) { name ->
            binding.tvUserName.text = name
        }

        // Example update profile (replace with actual data update mechanism)
        viewModel.updateProfile(R.drawable.logo, "New Store Name")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}