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
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivityLoginBinding
import capstone.tim.aireal.response.DataLogin
import capstone.tim.aireal.ui.regis.RegisterActivity
import capstone.tim.aireal.utils.InputValidator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var pref: UserPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide() // Sembunyikan action bar bawaan (jika ada)

        pref = UserPreference.getInstance(dataStore)
        setupViewModel()
        setupInputListeners()
        playAnimation()
        setUpAction()
         // Periksa status login di awal
    }


    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this, ViewModelFactory(pref, applicationContext))[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this, ::showLoading) // Tampilkan loading indicator

        loginViewModel.loginResult.observe(this) { loginResponse ->
            showLoading(false) // Sembunyikan loading indicator setelah respons diterima
            loginResponse?.let { response ->
                if (!response.error) {
                    // Login berhasil, simpan data pengguna ke preferensi
                    loginViewModel.saveUser(UserModel(
                        name = response.data?.name ?: "",
                        email = response.data?.email ?: "",
                        userId = response.data?.id ?: "",
                        token = response.token ?: "",
                        isLogin = true
                    ))
                } else {
                    // Login gagal
                    showErrorToast(response.message ?: "Login gagal")
                }
            } ?: showErrorToast("Terjadi kesalahan saat login")
        }
    }


    private fun setUpAction() {
        binding.btnlogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(DataLogin(email, password))
            } else {
                showErrorToast(getString(R.string.message_empty_field)) // Pesan error jika field kosong
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupInputListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.edtEmail.error = null
                binding.edtPassword.error = null
                buttonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.edtEmail.addTextChangedListener(textWatcher)
        binding.edtPassword.addTextChangedListener(textWatcher)
    }

    private fun buttonEnable() {
        val emailEditText = binding.edtEmail.text.toString()
        val passwordEditText = binding.edtPassword.text.toString()
        binding.btnlogin.isEnabled = InputValidator.isValidEmail(emailEditText) && passwordEditText.isNotEmpty()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val imagelogo = ObjectAnimator.ofFloat(binding.imageLogo, View.ALPHA, 1f).setDuration(500)
        val edtemail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtpassword =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
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

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
