package capstone.tim.aireal.ui.shopDisplay

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import capstone.tim.aireal.databinding.ActivityShopDisplayBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.ShopData
import com.bumptech.glide.Glide

class ShopDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val shopInformation = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, ShopData::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_DATA)
        }

        Log.d("ShopDisplay", "onCreate: $shopInformation")

        binding.apply {
            shopName.text = shopInformation?.name
            shopLocation.text = shopInformation?.street + ", " + shopInformation?.city + ", " + shopInformation?.province
            Glide.with(binding.root)
                .load(shopInformation?.imageUrl)
                .into(shopImage)

            imageView5.setOnClickListener {
                finish()
            }

            shareButton.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT,"Hey check out this great Shop: ${shopInformation?.name} on Aireal!")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent,"Share To:"))
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}