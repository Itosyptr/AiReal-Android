package capstone.tim.aireal.ui.editProfile

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import capstone.tim.aireal.databinding.ActivityEditProfileBinding
import capstone.tim.aireal.response.DataUser
import capstone.tim.aireal.ui.detailEdit.DetailEditActivity
import capstone.tim.aireal.utils.getImageUri
import capstone.tim.aireal.utils.reduceFileImage
import capstone.tim.aireal.utils.uriToFile
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Calendar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var pref: UserPreference
    private var token: String = ""
    private var userId: String = ""
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
                getString(R.string.full_name) -> {
                    binding.userName.text = selectedValue
                }

                getString(R.string.user_name) -> {
                    binding.name.text = selectedValue
                }

                getString(R.string.birthdate) -> {
                    binding.userBirthdate.text = selectedValue
                }

                getString(R.string.email) -> {
                    binding.userEmail.text = selectedValue
                }

                getString(R.string.password) -> {
                    binding.userPassword.text = selectedValue
                }

                getString(R.string.phone_number) -> {
                    binding.userPhone.text = selectedValue
                }

                getString(R.string.address) -> {
                    binding.userAddress.text = selectedValue
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!cameraPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        supportActionBar?.hide()

        val detailUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DETAIL_USER, DataUser::class.java)
        } else {
            intent.getParcelableExtra(DETAIL_USER)
        }

        pref = UserPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[EditProfileViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            token = "Bearer ${user.token}"
            userId = user.userId
        }

        binding.apply {
            userName.text = detailUser?.name
            name.text = detailUser?.username
            userEmail.text = detailUser?.email
            userPhone.text = detailUser?.phoneNumber
            userAddress.text = detailUser?.address
            binding.userGender.text = detailUser?.gender
            Glide.with(this@EditProfileActivity)
                .load(detailUser?.imageUrl?.get(0))
                .into(binding.profileImage)

            backButton.setOnClickListener {
                showConfirmationDialog(R.string.cancelled_confirmation, 0)
            }

            saveChanges.setOnClickListener {
                showConfirmationDialog(R.string.save_changes_confirmation, 1)
            }

            uploadImage.setOnClickListener {
                showCameraDialog()
            }

            cardInputName.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.full_name))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.userName.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputUsername.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.user_name))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.name.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputGender.setOnClickListener {
                showGenderConfirmationDialog()
            }

            cardInputBirthdate.setOnClickListener {
                val c = Calendar.getInstance()

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog =
                    DatePickerDialog(this@EditProfileActivity, { _, year, month, day ->
                        val selectedDate = String.format("%02d/%02d/%d", year, month + 1, day)
                        binding.userBirthdate.text = selectedDate
                    }, year, month, day)

                datePickerDialog.show()
            }

            cardInputEmail.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.email))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.userEmail.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputPassword.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.password))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.userPassword.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputPhone.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.phone_number))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.userPhone.text.toString())
                resultLauncher.launch(intent)
            }

            cardInputAddress.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.address))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.userAddress.text.toString())
                resultLauncher.launch(intent)
            }
        }
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
            binding.profileImage.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()

            val name = binding.userName.text.toString()
            val email = binding.userEmail.text.toString()
            val password = binding.userPassword.text.toString()
            val username = binding.userName.text.toString()
            val phoneNumber = binding.userPhone.text.toString()
            val address = binding.userAddress.text.toString()
            val gender = binding.userGender.text.toString()

            val requestName = name.toRequestBody("text/plain".toMediaType())
            val requestEmail = email.toRequestBody("text/plain".toMediaType())
            val requestUsername = username.toRequestBody("text/plain".toMediaType())
            val requestPassword = password.toRequestBody("text/plain".toMediaType())
            val requestPhoneNumber = phoneNumber.toRequestBody("text/plain".toMediaType())
            val requestAddress = address.toRequestBody("text/plain".toMediaType())
            val requestGender = gender.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("image", imageFile.name, requestImageFile)

            viewModel.updateProfile(
                token,
                userId,
                requestName,
                requestEmail,
                requestPassword,
                requestUsername,
                requestGender,
                requestAddress,
                requestPhoneNumber,
                multipartBody
            )

            viewModel.isLoading.observe(this) {
                if (it == false) {
                    finish()
                }
            }
        }
    }

    private fun showGenderConfirmationDialog() {
        val genders = arrayOf(getString(R.string.man), getString(R.string.woman))

        val builder = AlertDialog.Builder(this)
        builder.setItems(genders) { _, which ->
            if (which == 0) {
                Log.d("Gender", "Man Selected")
                binding.userGender.text = genders[0]
            } else {
                Log.d("Gender", "Woman Selected")
                binding.userGender.text = genders[1]
            }
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
                if (binding.userPassword.text.toString().isEmpty()) {
                    Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                } else if (binding.userEmail.text.toString().isEmpty()) {
                    Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                } else if (currentImageUri == null) {
                    Toast.makeText(
                        this,
                        "You should edit profile picture or cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    uploadImage()
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

    companion object {
        const val DETAIL_USER = "detail_user"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}