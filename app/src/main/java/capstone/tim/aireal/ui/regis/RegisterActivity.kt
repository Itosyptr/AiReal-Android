package capstone.tim.aireal.ui.regis

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivityRegisterBinding
import capstone.tim.aireal.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        registerViewModel.registerResult.observe(this) { response ->
            showLoading(false)
            response?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                if ((it.status ?: true) as Boolean) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                registerViewModel.registerResult.observe(this) { response ->
                    response?.let {
                        // Handle successful registration (e.g., navigate to login screen)
                        // Save the token (it.token) in SharedPreferences or a data store
                    }
                }


                registerViewModel.errorMessage.observe(this) { errorMessage ->
                    errorMessage?.let {
                        // Display the error message to the user (e.g., in a Toast)
                    }
                }


                registerViewModel.isLoading.observe(this) { loading ->
                    // Show/hide loading indicator based on the value of `loading`
                }


            }
        }
        setUpAction()
        playAnimation()
        buttonEnable()
        passwordEditText()
    }



    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateMinLength(password: String): Boolean {
        return password.length >= 6 // Atau panjang minimum yang Anda inginkan
    }

    private fun setUpAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            when {
                name.isEmpty() -> Toast.makeText(this, getString(R.string.massage_name), Toast.LENGTH_SHORT).show()
                email.isEmpty() -> Toast.makeText(this, getString(R.string.massage_email), Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(this, getString(R.string.massage_password), Toast.LENGTH_SHORT).show()
                else -> {
                    registerViewModel.register(name, email, password) // Menambahkan argumen yang diperlukan
                    showLoading(true)
                }
            }
        }

        binding.btnBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun buttonEnable() {
        val emailEditText = binding.edtEmail.text
        val passwordEditText = binding.edtPassword.text
        binding.btnRegister.isEnabled = isValidEmail(emailEditText.toString()) && validateMinLength(passwordEditText.toString())
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
