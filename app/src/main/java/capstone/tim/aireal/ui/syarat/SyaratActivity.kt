package capstone.tim.aireal.ui.syarat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivitySyaratBinding

class SyaratActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySyaratBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySyaratBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur toolbar (jika ada)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.terms_and_conditions)
    }

    // Handle tombol back di toolbar (jika ada)
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}