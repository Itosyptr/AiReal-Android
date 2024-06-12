package capstone.tim.aireal.ui.akun


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.FragmentAkunBinding
import capstone.tim.aireal.ui.editProfile.EditProfileActivity
import capstone.tim.aireal.ui.kebijakan.KebijakanActivity
import capstone.tim.aireal.ui.login.LoginActivity
import capstone.tim.aireal.ui.orderHistory.OrderHistoryActivity
import capstone.tim.aireal.ui.pusatinformasi.PusatInformasiActivity
import capstone.tim.aireal.ui.syarat.SyaratActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("DEPRECATION")
class AkunFragment : Fragment() {
    private lateinit var viewModel: AkunViewModel
    private var userId: String = ""
    private var token: String = ""
    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)

        pref = UserPreference.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, requireContext())
        )[AkunViewModel::class.java]

        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            userId = user?.userId.toString()
            token = user?.token.toString()

            val bearerToken = "Bearer $token"
            viewModel.fetchUserProfile(bearerToken, userId)
        }
        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.nameTextView.text = user.name

                Glide.with(this)
                    .load(user.imageUrl?.get(0))
                    .apply(RequestOptions.bitmapTransform(CircleCrop())) // Mengubah gambar menjadi bulat
                    .error(R.drawable.profile_image)
                    .into(binding.imageView)
            }
        }


        binding.textViewEditProfile.setOnClickListener {
            val intent = Intent(
                requireContext(),
                EditProfileActivity::class.java
            )  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.textViewPesananSaya.setOnClickListener {
            val intent = Intent(
                requireContext(),
                OrderHistoryActivity::class.java
            )  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.textViewPusatInformasi.setOnClickListener {
            val intent = Intent(
                requireContext(),
                PusatInformasiActivity::class.java
            )  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.textViewSyaratKetentuan.setOnClickListener {
            val intent = Intent(
                requireContext(),
                SyaratActivity::class.java
            )  // Use requireContext() to get the context
            startActivity(intent)
        }
        binding.textViewKebijakanPrivasi.setOnClickListener {
            val intent = Intent(
                requireContext(),
                KebijakanActivity::class.java
            )  // Use requireContext() to get the context
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                pref.logout() // Log the user out in DataStore

                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        return binding.root
    }

}