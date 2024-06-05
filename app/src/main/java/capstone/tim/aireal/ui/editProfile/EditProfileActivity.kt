package capstone.tim.aireal.ui.editProfile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivityEditProfileBinding
import capstone.tim.aireal.ui.detailEdit.DetailEditActivity
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == DetailEditActivity.RESULT_CODE && result.data != null) {
            val selectedValue = result.data?.getStringExtra(DetailEditActivity.RESULT_EDIT)

            when (result.data?.getStringExtra(DetailEditActivity.TYPE)) {
                getString(R.string.full_name) -> {
                    binding.userName.text = selectedValue
                }

                getString(R.string.birthdate) -> {
                    binding.userBirthdate.text = selectedValue
                }

                getString(R.string.email) -> {
                    binding.userEmail.text = selectedValue
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

            cardInputName.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, DetailEditActivity::class.java)
                intent.putExtra(DetailEditActivity.EXTRA_TITLE, getString(R.string.full_name))
                intent.putExtra(DetailEditActivity.EXTRA_HINT, binding.userName.text.toString())
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