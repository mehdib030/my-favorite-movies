package com.e.myfavoritemovies.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {

    @JvmStatic
    fun formatDate(dateString: String): String {
        var d: Date? = null
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            d = sdf.parse(dateString)
        } catch (ex: ParseException) {
            Log.v("Exception", ex.localizedMessage ?: "Unknown parse error")
        }
        sdf.applyPattern("MMM dd yyyy")
        return sdf.format(d)
    }
}
