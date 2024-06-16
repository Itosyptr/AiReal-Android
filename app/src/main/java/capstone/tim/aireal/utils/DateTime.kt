package capstone.tim.aireal.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DateTime {

    fun getDate(dateString: String): String? {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(dateString)

            val outputFormat = SimpleDateFormat("EEEE, dd MM yyyy", Locale.getDefault())
            date?.let {
                outputFormat.format(it)
            } ?: run {
                null
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}