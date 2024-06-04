package capstone.tim.aireal.ui.login

import capstone.tim.aireal.R
import capstone.tim.aireal.MainActivity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import capstone.tim.aireal.ui.regis.RegisterFragment
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    private val Context.dataStore by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(requireContext().dataStore)))[LoginViewModel::class.java]

        playAnimation()
        setUpAction()
        buttonEnable()
        passwordEditText()

        loginViewModel.getLogin().observe(viewLifecycleOwner) {
            showLoading(false)
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            if (!it.error) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text?.toString()
            val password = binding.edtPassword.text?.toString()
            when {
                email.isNullOrEmpty() -> {
                    Toast.makeText(requireContext(), getString(R.string.massage_email), Toast.LENGTH_SHORT).show()
                }
                password.isNullOrEmpty() -> {
                    Toast.makeText(requireContext(), getString(R.string.massage_password), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    setUpLogin()
                    showLoading(true)
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(requireContext(), RegisterFragment::class.java)
            startActivity(intent)
        }
    }

    private fun setUpLogin() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        loginViewModel.login(email, password)
    }

    private fun buttonEnable() {
        val emailEditText = binding.edtEmail.text?.toString()
        val passwordEditText = binding.edtPassword.text?.toString()
        binding.btnLogin.isEnabled =
            isValidEmail(emailEditText ?: "") && validateMinLegth(passwordEditText ?: "")
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.iv2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val imagelogo = ObjectAnimator.ofFloat(binding.iv2, View.ALPHA, 1f).setDuration(500)
        val edtemail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtpassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvaccount = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, 1f).setDuration(500)
        val tvregister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(edtemail, edtpassword, tvaccount, tvregister)
        }

        AnimatorSet().apply {
            playSequentially(imagelogo, btnlogin, together)
            startDelay = 500
        }.start()
    }

    private fun showLoading(loading: Boolean) { binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE }

    private fun passwordEditText() {
        binding.edtPassword.addTextChangedListen(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}