package capstone.tim.aireal.ui.addProduct

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivityAddProductBinding
import capstone.tim.aireal.ui.detailEdit.DetailEditActivity
import capstone.tim.aireal.utils.getImageUri

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: AddProductViewModel
    private lateinit var pref: UserPreference
    private var token: String = ""
    private var categoryProduct = ""
    private var currentImageUri: Uri? = null
    private var listImageUri: ArrayList<Uri> = arrayListOf()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, R.string.granted, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.denied, Toast.LENGTH_LONG).show()
            }
        }

    private fun cameraPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

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

                getString(R.string.product_long_description_example) -> {
                    binding.productLongDescriptionInput.text = selectedValue
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

        if (!cameraPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        supportActionBar?.hide()

        val shopId = intent.getStringExtra(SHOP_ID)

        pref = UserPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[AddProductViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            token = "Bearer ${user.token}"
        }

        binding.apply {
            backButton.setOnClickListener {
                showConfirmationDialog(R.string.cancelled_add_product, 0)
            }

            uploadImage.setOnClickListener {
                showCameraDialog()
            }

            cancelButton.setOnClickListener {
                showConfirmationDialog(R.string.cancelled_add_product, 0)
            }

            save.setOnClickListener {
                showConfirmationDialog(R.string.create_new_product, 1)
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

            productLongDescription.setOnClickListener {
                val intent = Intent(this@AddProductActivity, DetailEditActivity::class.java)
                intent.putExtra(
                    DetailEditActivity.EXTRA_TITLE,
                    getString(R.string.product_long_description_example)
                )
                intent.putExtra(
                    DetailEditActivity.EXTRA_HINT,
                    binding.productLongDescriptionInput.text.toString()
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

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImage.layoutManager = layoutManager
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherCamera.launch(currentImageUri!!)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            listImageUri.add(it)
            setImageData(listImageUri)
        }
    }

    private fun showCategoryConfirmationDialog() {
        val category = resources.getStringArray(R.array.product_categories)
        val categoryId = resources.getStringArray(R.array.product_categories_id)

        val builder = AlertDialog.Builder(this)
        builder.setItems(category) { _, which ->
            binding.productCategoryInput.text = category.getOrNull(which) ?: ""
            categoryProduct = categoryId.getOrNull(which) ?: ""
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

        builder.setView(view)
            .setPositiveButton(null, null)
            .setNegativeButton(null, null)

        val dialog = builder.create()
        dialog.show()

        val cardCamera = view.findViewById<CardView>(R.id.choose_camera)
        val cardGallery = view.findViewById<CardView>(R.id.choose_gallery)

        cardCamera.setOnClickListener {
            startCamera()
            dialog.dismiss()
        }

        cardGallery.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }
    }

    private fun setImageData(listImageUri: ArrayList<Uri>) {
        val adapter = ImageAdapter(listImageUri)
        binding.rvImage.adapter = adapter
    }

    companion object {
        const val SHOP_ID = "shop_id"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}