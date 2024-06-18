package capstone.tim.aireal.ui.pusatinformasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivityPusatInformasiBinding

class PusatInformasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPusatInformasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPusatInformasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.pusat_informasi)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
