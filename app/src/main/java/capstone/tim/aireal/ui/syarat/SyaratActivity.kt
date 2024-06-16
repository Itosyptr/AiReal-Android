package capstone.tim.aireal.ui.syarat

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivitySyaratBinding

@Suppress("DEPRECATION")
class SyaratActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySyaratBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyaratBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur judul ActionBar
        supportActionBar?.title = getString(R.string.syarat_dan_ketentuan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Menampilkan teks dari string resources
        binding.apply {
            tvIsiSyarat.text = getString(R.string.selamat_datang_di_aireal)
            tvDetailAkunPengguna.text = getString(R.string.detail_akun_pengguna)
            tvDetailPenggunaanAplikasi.text = getString(R.string.detail_penggunaan_aplikasi)
            tvDetailTransaksi.text = getString(R.string.detail_transaksi)
            tvDetailHakKekayaanIntelektual.text = getString(R.string.detail_hak_kekayaan_intelektual)
            tvDetailPembatasanTanggungJawab.text = getString(R.string.detail_pembatasan_tanggung_jawab)
            tvDetailPerubahanSyarat.text = getString(R.string.detail_perubahan_syarat)
            tvDetailHukumYangBerlaku.text = getString(R.string.detail_hukum_yang_berlaku)
            tvDetailKetentuanLain.text = getString(R.string.detail_ketentuan_lain)
            tvPersetujuan.text = getString(R.string.persetujuan)
        }
    }

    // Handle tombol back di ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
