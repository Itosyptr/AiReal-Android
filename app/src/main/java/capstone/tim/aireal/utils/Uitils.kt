package capstone.tim.aireal.utils


import android.text.TextUtils
import android.util.Patterns
import capstone.tim.aireal.data.cekpass.CekPass

object InputValidator {

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



    fun validateMinLegth(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= CekPass.MIN_LENGTH_PASSWORD
    }
}