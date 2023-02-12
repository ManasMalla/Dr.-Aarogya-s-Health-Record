package com.manasmalla.draarogyashealthrecord.ui.screens

import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.User
import com.manasmalla.draarogyashealthrecord.model.heightUnits
import com.manasmalla.draarogyashealthrecord.model.weightUnits

data class UserUiState(
    val name: String,
    val age: String,
    val gender: Gender,
    val image: String? = null,
    val metric: List<Metrics> = listOf(),
    val weightUnit: Int = 0,
    val heightUnit: Int = 0,
    val actionsEnabled: Boolean = false
)

val UserUiState.formattedAge: String get() = if (this.age.toIntOrNull() == null || this.age.toIntOrNull() == 0) "" else this.age

val UserUiState.isValid get() = this.name.isNotBlank() && this.age.isNotEmpty() && this.age.toIntOrNull() != null && metric.isNotEmpty()

fun UserUiState.toUser(): User = User(
    name = this.name,
    age = this.age.toInt(),
    gender = this.gender.name,
    metric = this.metric,
    image = this.image,
    weightUnit = if (this.metric.contains(Metrics.Weight)) weightUnits[this.weightUnit] else "",
    heightUnit = if (this.metric.contains(Metrics.Height)) heightUnits[this.heightUnit] else ""
)
