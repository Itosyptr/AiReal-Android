package capstone.tim.aireal.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import capstone.tim.aireal.MainActivity
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivityLoginBinding
import capstone.tim.aireal.ui.regis.RegisterActivity
import capstone.tim.aireal.utils.InputValidator

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        loginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]

        playAnimation()
        setUpAction()
        buttonEnable()
        passwordEditText()

        loginViewModel.getLogin().observe(this) {
            showLoading(false)
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            if (!it.error) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setUpAction() {
        binding.btnlogin.setOnClickListener {
            val email = binding.edtEmail.text?.toString()
            val password = binding.edtPassword.text?.toString()
            when {
                email.isNullOrEmpty() -> {
                    Toast.makeText(this, getString(R.string.massage_email), Toast.LENGTH_SHORT).show()
                }
                password.isNullOrEmpty() -> {
                    Toast.makeText(this, getString(R.string.massage_password), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    setUpLogin()
                    showLoading(true)
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
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
        binding.btnlogin.isEnabled =
            InputValidator.isValidEmail(emailEditText ?: "") &&
                    InputValidator.validateMinLegth(passwordEditText ?: "")
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val imagelogo = ObjectAnimator.ofFloat(binding.imageLogo, View.ALPHA, 1f).setDuration(500)
        val edtemail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtpassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnlogin, View.ALPHA, 1f).setDuration(500)
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
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonEnable()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}