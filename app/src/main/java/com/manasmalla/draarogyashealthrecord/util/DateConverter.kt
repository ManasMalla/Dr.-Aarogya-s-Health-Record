package com.manasmalla.draarogyashealthrecord.util

import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date

class DateConverter {

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
