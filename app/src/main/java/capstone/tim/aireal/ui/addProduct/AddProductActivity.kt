package capstone.tim.aireal.ui.addProduct

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivityAddProductBinding
import capstone.tim.aireal.ui.detailEdit.DetailEditActivity

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == DetailEditActivity.RESULT_CODE && result.data != null) {
            val selectedValue = result.data?.getStringExtra(DetailEditActivity.RESULT_EDIT)

            when (result.data?.getStringExtra(DetailEditActivity.TYPE)) {
                getString(R.string.product_name) -> {
                    binding.productNameInput.text = selectedValue
                }

                getString(R.string.product_description_example) -> {
                    binding.productDescriptionInput.text = selectedValue
                }

                getString(R.string.price) -> {
                    binding.productPriceInput.text = selectedValue
                }

                getString(R.string.product_stock) -> {
                    binding.productStockInput.text = selectedValue
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            backButton.setOnClickListener {
                showConfirmationDialog(R.string.cancelled_add_product, 0)
            }

            uploadImage.setOnClickListener {
                showCameraDialog()
            }

            productName.setOnClickListener {
                val intent = Intent(this@AddProductActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.product_name))
                intent.putExtra(
                    DetailEditActivity.EXTRA_HINT,
                    binding.productNameInput.text.toString()
                )
                resultLauncher.launch(intent)
            }

            productDescription.setOnClickListener {
                val intent = Intent(this@AddProductActivity, DetailEditActivity::class.java)
                intent.putExtra(
                    DetailEditActivity.EXTRA_TITLE,
                    getString(R.string.product_description_example)
                )
                intent.putExtra(
                    DetailEditActivity.EXTRA_HINT,
                    binding.productDescriptionInput.text.toString()
                )
                resultLauncher.launch(intent)
            }

            productPrice.setOnClickListener {
                val intent = Intent(this@AddProductActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.price))
                intent.putExtra(
                    DetailEditActivity.EXTRA_HINT,
                    binding.productPriceInput.text.toString()
                )
                resultLauncher.launch(intent)
            }

            productStock.setOnClickListener {
                val intent = Intent(this@AddProductActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.product_stock))
                intent.putExtra(
                    DetailEditActivity.EXTRA_HINT,
                    binding.productStockInput.text.toString()
                )
                resultLauncher.launch(intent)
            }

            productCategory.setOnClickListener {
                showCategoryConfirmationDialog()
            }
        }
    }

    private fun showCategoryConfirmationDialog() {
        val category = resources.getStringArray(R.array.product_categories)

        val builder = AlertDialog.Builder(this)
        builder.setItems(category) { _, which ->
            binding.productCategoryInput.text = category.getOrNull(which) ?: ""
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
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