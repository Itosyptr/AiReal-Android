package capstone.tim.aireal


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivitySplashBinding
import capstone.tim.aireal.ui.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var pref: UserPreference
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        pref = UserPreference.getInstance(dataStore)

        // Mengatur sumber video
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.videooo)
        binding.video.setVideoURI(videoUri)

        // Menambahkan MediaController
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.video)
        binding.video.setMediaController(mediaController)

        // Mulai memutar video setelah persiapan selesai
        binding.video.setOnPreparedListener { mp ->
            mp.start()
            mp.isLooping = true
        }

        // Memulai pemutaran video secara otomatis
        binding.video.start()

        // Animasi fade-in untuk VideoView


        mediaPlayer = MediaPlayer.create(this, videoUri).apply {
            setOnPreparedListener { start() }
            setOnCompletionListener { release() }
            isLooping = true
        }
        binding.video.setOnClickListener { mediaPlayer?.start() }


        // Memulai coroutine untuk pengecekan login setelah delay
        lifecycleScope.launch {
            delay(3000) // Contoh delay 2 detik
            checkLogin()
        }
    }

    private fun checkLogin() {
        lifecycleScope.launch {
            if (pref.isLoggedIn()) {
                startMainActivity()
            } else {
                startLoginActivity()
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startLoginActivity() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

