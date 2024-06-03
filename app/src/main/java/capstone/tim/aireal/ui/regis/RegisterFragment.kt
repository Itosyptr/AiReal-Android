package capstone.tim.aireal.ui.regis

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.FragmentRegisterBinding
import capstone.tim.aireal.ui.login.LoginFragment

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]

        playAnimation()
        setUpAction()
        buttonEnable()
        passwordEditText()

        registerViewModel.getRegister().observe(viewLifecycleOwner) {
            if (it == null) {
                showLoading(true)
            } else {
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
                val intent = Intent(requireContext(), LoginFragment::class.java)
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivregis, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val btnbacktologin = ObjectAnimator.ofFloat(binding.btnBackToLogin, View.ALPHA, 1f).setDuration(500)
        val imagelogo = ObjectAnimator.ofFloat(binding.ivregis, View.ALPHA, 1f).setDuration(500)
        val edtname = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(500)
        val edtemail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtpassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(edtname, edtemail, edtpassword)
        }

        AnimatorSet().apply {
            playSequentially(btnbacktologin, imagelogo, btnRegister, together)
            startDelay = 500
        }.start()
    }

    private fun setUpAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            when {
                name.isEmpty() -> Toast.makeText(requireContext(), getString(R.string.massage_name), Toast.LENGTH_SHORT).show()
                email.isEmpty() -> Toast.makeText(requireContext(), getString(R.string.massage_email), Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(requireContext(), getString(R.string.massage_password), Toast.LENGTH_SHORT).show()
                else -> {
                    registerViewModel.register(name, email, password)
                    showLoading(true)
                }
            }
        }
        binding.btnBackToLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginFragment::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun buttonEnable() {
        val emailEditText = binding.edtEmail.text
        val passwordEditText = binding.edtPassword.text
        binding.btnRegister.isEnabled = isValidEmail(emailEditText.toString()) && validateMinLegth(passwordEditText.toString())
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun passwordEditText() {
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonEnable()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}