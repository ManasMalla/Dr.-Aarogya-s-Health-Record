package com.manasmalla.draarogyashealthrecord.ui.screens


enum class Gender{Male, Female, Other}
enum class Metrics{Calories, FatPercentage, WaterPercentage, MuscleMass, BoneMass, Weight, Height, BloodSugar, BloodPressure}

data class UserUiState(val name: String, val age: Int, val gender: Gender, val metric: List<Metrics>, val weightUnit: Int = 0, val heightUnit: Int = 0, val actionsEnabled: Boolean = false)

val UserUiState.formattedAge:String get() = if(this.age == 0) "" else this.age.toString()

val UserUiState.isValid get() = this.name.isNotBlank() && this.age > 0 && metric.isNotEmpty()
