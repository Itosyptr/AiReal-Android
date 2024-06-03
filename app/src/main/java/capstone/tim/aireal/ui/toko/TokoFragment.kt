package capstone.tim.aireal.ui.toko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentTokoBinding

class TokoFragment : Fragment() {

    private var _binding: FragmentTokoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TokoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTokoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TokoViewModel::class.java)

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        viewPager.adapter = SectionPagerAdapter(this)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Profile"
                1 -> "Another Tab" // Replace with actual tab names
                else -> "Tab ${position + 1}"
            }
        }.attach()

        viewModel.profileImage.observe(viewLifecycleOwner, Observer { imageResId ->
            binding.profileImageView.setImageResource(imageResId)
        })

        viewModel.profileName.observe(viewLifecycleOwner, Observer { name ->
            binding.profileNameTextView.text = name
        })

        // Example update profile (replace with actual data update mechanism)
        viewModel.updateProfile(R.drawable.new_profile_image, "New Store Name")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
