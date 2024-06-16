package capstone.tim.aireal.ui.pusatinformasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivityPusatInformasiBinding

@Suppress("DEPRECATION")
class PusatInformasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPusatInformasiBinding
    private lateinit var informasiAdapter: InformasiAdapter // Definisikan adapter nanti

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPusatInformasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur action bar (jika diperlukan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.pusat_informasi)

        // Inisialisasi adapter
        informasiAdapter = InformasiAdapter(getInformasiList())

        // Setup RecyclerView
        binding.rvPusatInformasi.apply {
            layoutManager = LinearLayoutManager(this@PusatInformasiActivity)
            adapter = informasiAdapter
        }
    }

    // Fungsi untuk mendapatkan daftar informasi dari sumber data
    private fun getInformasiList(): List<InformasiItem> {
        // Gantilah bagian ini dengan logika untuk mengambil data informasi dari
        // sumber data yang Anda gunakan (misalnya, database, file JSON, dll.)
        return listOf(
            InformasiItem("Judul Informasi 1", "Isi informasi mengenai topik 1."),
            InformasiItem("Judul Informasi 2", "Isi informasi mengenai topik 2."),
            // Tambahkan lebih banyak item informasi sesuai kebutuhan
        )
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}