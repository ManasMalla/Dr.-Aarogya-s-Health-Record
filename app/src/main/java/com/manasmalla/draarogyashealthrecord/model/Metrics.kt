package com.manasmalla.draarogyashealthrecord.model


enum class Metrics { Calories, FatPercentage, WaterPercentage, MuscleMass, BoneMass, Weight, Height, BloodSugar, BloodPressure, BMI }

val Metrics.unit: String
    get() {
        return when (this) {
            Metrics.Calories -> "kcal"
            Metrics.FatPercentage, Metrics.WaterPercentage -> "%"
            Metrics.MuscleMass -> "kgs"
            Metrics.BoneMass -> "kgs"
            Metrics.Weight -> "kgs"
            Metrics.Height -> "cm"
            Metrics.BloodSugar -> ""
            Metrics.BloodPressure -> "bpm"
            Metrics.BMI -> ""
        }
    }


val weightUnits = listOf("kgs", "lbs")
val heightUnits = listOf("cms", "ft", "in")