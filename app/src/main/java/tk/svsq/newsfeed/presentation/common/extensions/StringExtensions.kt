package tk.svsq.newsfeed.presentation.common.extensions

import android.content.Context
import android.widget.Toast
import tk.svsq.newsfeed.DATE_TIME_PATTERN_UI
import tk.svsq.newsfeed.ISO_DATE_TIME_PATTERN
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.showAsToast(context: Context?, isLong: Boolean = false) {
    Toast.makeText(
        context,
        this,
        if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}

fun String.toDateTimeString(): String? {
    val parser = SimpleDateFormat(ISO_DATE_TIME_PATTERN, Locale.getDefault())
    val formatter = SimpleDateFormat(DATE_TIME_PATTERN_UI, Locale.getDefault())
    return try {
        parser.parse(this)?.let {
            formatter.format(it)
        }

    } catch (ex: ParseException) {
        ex.printStackTrace()
        null
    }
}

fun emptyString() = ""