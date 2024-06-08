package capstone.tim.aireal.ui.akun

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentAkunBinding
import capstone.tim.aireal.ui.editProfile.EditProfileActivity
import capstone.tim.aireal.ui.kebijakan.KebijakanActivity
import capstone.tim.aireal.ui.login.LoginActivity
import capstone.tim.aireal.ui.orderHistory.OrderHistoryActivity
import capstone.tim.aireal.ui.pusatinformasi.PusatInformasiActivity
import capstone.tim.aireal.ui.syarat.SyaratActivity

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
        viewModel.updateProfile(R.drawable.profile_image, "New Name")
        binding.textViewPesananSaya.setOnClickListener {
            val intent = Intent(requireContext(), OrderHistoryActivity::class.java)  // Use requireContext() to get the context
            startActivity(intent)
        }

        binding.textViewEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.textViewPusatInformasi.setOnClickListener {
            val intent = Intent(requireContext(), PusatInformasiActivity::class.java)  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.textViewSyaratKetentuan.setOnClickListener {
            val intent = Intent(requireContext(), SyaratActivity::class.java)  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.textViewKebijakanPrivasi.setOnClickListener {
            val intent = Intent(requireContext(), KebijakanActivity::class.java)  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)  // Use requireContext() to get the context
            startActivity(intent)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
