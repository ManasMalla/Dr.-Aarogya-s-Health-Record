package com.manasmalla.draarogyashealthrecord.util

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateConverter {

    val dateFormatters =
        arrayOf(
            SimpleDateFormat("EEEE", Locale.getDefault()),
            SimpleDateFormat("dd", Locale.getDefault()),
            SimpleDateFormat("MMM yy", Locale.getDefault())
        )

    @TypeConverter
    fun fromDate(value: Date): String {
        return value.time.toString()
    }

    @TypeConverter
    fun toDate(value: String): Date {
        return try {
            val time = value.toLong()
            Date(time)
        } catch (e: Exception) {
            Calendar.getInstance().time
        }
    }
}
