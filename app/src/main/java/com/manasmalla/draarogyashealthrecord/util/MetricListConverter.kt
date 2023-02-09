package com.manasmalla.draarogyashealthrecord.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manasmalla.draarogyashealthrecord.model.Metrics

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

class MetricListConverter {

    @TypeConverter
    fun fromMapOfMetricList(value: Map<Metrics, Double>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMapOfMetricList(value: String): Map<Metrics, Double> {
        return try {
            Gson().fromJson(value) //using extension function
        } catch (e: Exception) {
            mapOf()
        }
    }

    @TypeConverter
    fun fromStringMetricList(value: List<Metrics>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringMetricList(value: String): List<Metrics> {
        return try {
            Gson().fromJson(value) //using extension function
        } catch (e: Exception) {
            listOf()
        }
    }
}
