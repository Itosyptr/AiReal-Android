package capstone.tim.aireal.ui.editShop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivityEditShopBinding
import capstone.tim.aireal.ui.detailEdit.DetailEditActivity

class EditShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditShopBinding

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == DetailEditActivity.RESULT_CODE && result.data != null) {
            val selectedValue = result.data?.getStringExtra(DetailEditActivity.RESULT_EDIT)

            when (result.data?.getStringExtra(DetailEditActivity.TYPE)) {
                getString(R.string.shop_name) -> {
                    binding.shopName.text = selectedValue
                }

                getString(R.string.shop_description) -> {
                    binding.shopDescription.text = selectedValue
                }

                getString(R.string.shop_address) -> {
                    binding.shopAddress.text = selectedValue
                }

                getString(R.string.regency) -> {
                    binding.shopRegency.text = selectedValue
                }

                getString(R.string.province) -> {
                    binding.shopProvince.text = selectedValue
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            backButton.setOnClickListener {
                showConfirmationDialog(R.string.cancelled_confirmation, 0)
            }

            saveChanges.setOnClickListener {
                showConfirmationDialog(R.string.save_changes_confirmation, 1)
            }

            uploadImage.setOnClickListener {
                showCameraDialog()
            }

            cardInputShopname.setOnClickListener {
                val intent = Intent(this@EditShopActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.shop_name))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.shopName.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputDescription.setOnClickListener {
                val intent = Intent(this@EditShopActivity, DetailEditActivity::class.java)
                intent.putExtra(
                    DetailEditActivity.EXTRA_TITLE,
                    getString(R.string.shop_description)
                )
                intent.putExtra(
                    DetailEditActivity.EXTRA_HINT,
                    binding.shopDescription.text.toString()
                )
                resultLauncher.launch(intent)
            }

            cardInputDetailAddress.setOnClickListener {
                val intent = Intent(this@EditShopActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.shop_address))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.shopAddress.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputRegency.setOnClickListener {
                val intent = Intent(this@EditShopActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.regency))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.shopRegency.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputProvince.setOnClickListener {
                val intent = Intent(this@EditShopActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.province))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.shopProvince.text.toString())
                resultLauncher.launch(intent)
            }
        }
    }

    private fun showConfirmationDialog(message: Int, type: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            if (type == 0) {
                finish()
            } else {
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showCameraDialog() {
        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.choose_camera, null)

        val cardCamera = view.findViewById<CardView>(R.id.choose_camera)
        val cardGallery = view.findViewById<CardView>(R.id.choose_gallery)

        cardCamera.setOnClickListener {
            Log.d("Camera", "Camera Selected")
        }

        cardGallery.setOnClickListener {
            Log.d("Gallery", "Gallery Selected")
        }

        builder.setView(view)
            .setPositiveButton(null, null)
            .setNegativeButton(null, null)

        val dialog = builder.create()
        dialog.show()
    }
}