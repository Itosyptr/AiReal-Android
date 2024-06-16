package capstone.tim.aireal.ui.kebijakan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import capstone.tim.aireal.databinding.ActivityKebijakanBinding // Pastikan binding sudah digenerate

class KebijakanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKebijakanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKebijakanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur action bar (jika diperlukan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(capstone.tim.aireal.R.string.kebijakan_privasi) // Set judul dari strings.xml
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}