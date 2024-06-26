package capstone.tim.aireal.ui.editShop

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
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
import capstone.tim.aireal.R
import capstone.tim.aireal.ViewModelFactory
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.databinding.ActivityEditShopBinding
import capstone.tim.aireal.response.ShopData
import capstone.tim.aireal.ui.detailEdit.DetailEditActivity
import capstone.tim.aireal.utils.getImageUri
import capstone.tim.aireal.utils.reduceFileImage
import capstone.tim.aireal.utils.uriToFile
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditShopBinding
    private lateinit var viewModel: EditShopViewModel
    private lateinit var pref: UserPreference
    private var token: String = ""
    private var userId: String = ""
    private var shopId: String = ""
    private var typeEdit: Int = 0
    private var currentImageUri: Uri? = null

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

        if (!cameraPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        supportActionBar?.hide()

        typeEdit = intent.getIntExtra(TYPE, 0)
        if (typeEdit == 1) {

        } else {
            binding.textViewTitle.text = getString(R.string.edit_shop_true)
        }

        val detailShop = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DETAIL_SHOP, ShopData::class.java)
        } else {
            intent.getParcelableExtra(DETAIL_SHOP)
        }

        pref = UserPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[EditShopViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            token = "Bearer ${user.token}"
            userId = user.userId
        }

        shopId = detailShop?.id.toString() ?: ""

        binding.apply {
            shopName.text = detailShop?.name ?: ""
            shopDescription.text = detailShop?.description ?: ""
            shopAddress.text = detailShop?.street ?: ""
            shopRegency.text = detailShop?.city ?: ""
            shopProvince.text = detailShop?.province ?: ""
            if (detailShop?.imageUrl?.isNotEmpty() == true) {
                Glide.with(this@EditShopActivity)
                    .load(detailShop?.imageUrl.get(0))
                    .into(shopImage)
            }

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
        setupObservers()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            checkblur()
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
            checkblur()
        }
    }

    private fun setupObservers() {
        viewModel.modelresult.observe(this) { result ->
            if (result != null && currentImageUri != null) {
                if (result.is_blurry == false) {
                    customToast("Gambar Anda Jelas Dengan Akurasi ${result.percentage}")
                    showImage()
                } else {
                    customToastFailed("Gambar Kurang Jelas Dengan Akurasi ${result.percentage}")
                }

            }
        }

        viewModel.isLoading2.observe(this) { isLoading ->
            binding.progressText.visibility = if (isLoading) View.VISIBLE else View.GONE
            showLoading(isLoading)
        }
    }

    private fun checkblur() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBodyPart = MultipartBody.Part.createFormData("file", imageFile.name, requestImageFile)
            viewModel.checkblur(multipartBodyPart)
        }
    }



    private fun showImage() {
        currentImageUri?.let {
            binding.shopImage.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()

            val name = binding.shopName.text.toString()
            val description = binding.shopDescription.text.toString()
            val address = binding.shopAddress.text.toString()
            val regency = binding.shopRegency.text.toString()
            val province = binding.shopProvince.text.toString()

            val requestUserId = userId.toRequestBody("text/plain".toMediaType())
            val requestName = name.toRequestBody("text/plain".toMediaType())
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestAddress = address.toRequestBody("text/plain".toMediaType())
            val requestRegency = regency.toRequestBody("text/plain".toMediaType())
            val requestProvince = province.toRequestBody("text/plain".toMediaType())

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("image", imageFile.name, requestImageFile)

            if (typeEdit == 0) {
                viewModel.updateShop(
                    token,
                    shopId,
                    requestUserId,
                    requestName,
                    requestDescription,
                    requestAddress,
                    requestRegency,
                    requestProvince,
                    multipartBody
                )
            } else if (typeEdit == 1) {
                viewModel.craeteShop(
                    token,
                    requestUserId,
                    requestName,
                    requestDescription,
                    requestAddress,
                    requestRegency,
                    requestProvince,
                    multipartBody
                )
            }

            viewModel.isLoading.observe(this) {
                showLoading(it)
                if (it == false) {
                    customToast("Shop Updated")
                    finish()
                }
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
                when {
                    binding.shopName.text.toString().isEmpty() -> {
                        Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    }

                    binding.shopDescription.text.toString().isEmpty() -> {
                        Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                    }

                    binding.shopAddress.text.toString().isEmpty() -> {
                        Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show()
                    }

                    binding.shopRegency.text.toString().isEmpty() -> {
                        Toast.makeText(this, "Regency cannot be empty", Toast.LENGTH_SHORT).show()
                    }

                    binding.shopProvince.text.toString().isEmpty() -> {
                        Toast.makeText(this, "Province cannot be empty", Toast.LENGTH_SHORT).show()
                    }

                    currentImageUri == null -> {
                        Toast.makeText(
                            this,
                            "You should edit Shop picture or cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        uploadImage()
                    }
                }
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

    private fun customToast(text: String) {
        val customToastLayout = layoutInflater.inflate(R.layout.custom_toast_success,null)
        val customToast = Toast(this)
        customToast.view = customToastLayout
        customToastLayout.findViewById<TextView>(R.id.message_toast).text = text
        customToast.setGravity(Gravity.CENTER,0,0)
        customToast.duration = Toast.LENGTH_LONG
        customToast.show()
    }
    private fun customToastFailed(text: String) {
        val customToastLayout = layoutInflater.inflate(R.layout.custom_toast_failed,null)
        val customToast = Toast(this)
        customToast.view = customToastLayout
        customToastLayout.findViewById<TextView>(R.id.message_toast_fail).text = text
        customToast.setGravity(Gravity.CENTER,0,0)
        customToast.duration = Toast.LENGTH_LONG
        customToast.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DETAIL_SHOP = "detail_shop"
        const val TYPE = "type"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}