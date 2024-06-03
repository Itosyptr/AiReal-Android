package capstone.tim.aireal.ui.akun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentAkunBinding

@Suppress("DEPRECATION")
class AkunFragment : Fragment() {

    private lateinit var viewModel: AkunViewModel
    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AkunViewModel::class.java)

        viewModel.profileImage.observe(viewLifecycleOwner, Observer { imageResId ->
            binding.imageView.setImageResource(imageResId)
        })

        viewModel.profileName.observe(viewLifecycleOwner, Observer { name ->
            binding.nameTextView.text = name
        })

        // Update profile as an example (replace with real data update mechanism)
        viewModel.updateProfile(R.drawable.logo, "New Name")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
