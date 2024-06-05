package capstone.tim.aireal.ui.detailEdit

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ActivityDetailEditBinding

class DetailEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(EXTRA_TITLE)
        val formattedTitle = getString(R.string.change_title, title)
        binding.editTitle.text = formattedTitle

        val hint = intent.getStringExtra(EXTRA_HINT)
        binding.detailEditText.text = hint?.let { Editable.Factory.getInstance().newEditable(it) }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveChanges.setOnClickListener {
            val value = binding.detailEditText.text.toString()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()

            val resultIntent = Intent()
            resultIntent.putExtra(RESULT_EDIT, value)
            resultIntent.putExtra(TYPE, title)
            setResult(RESULT_CODE, resultIntent)
            finish()
        }
    }

    companion object {
        const val EXTRA_HINT = "extra_hint"
        const val EXTRA_TITLE = "extra_title"
        const val RESULT_EDIT = "RESULT_EDIT"
        const val TYPE = "type"
        const val RESULT_CODE = 110
    }
}