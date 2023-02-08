package com.manasmalla.draarogyashealthrecord.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val uId: Int = 0,
    val name: String,
    val age: Int,
    val gender: String,
    val metric: List<Metrics>,
    @ColumnInfo(name = "weight_unit") val weightUnit: String = "",
    @ColumnInfo(name = "height_unit") val heightUnit: String = "",
    @ColumnInfo(name = "is_current_user") val isCurrentUser: Boolean = true
)

fun User.toUiState(): UserUiState = UserUiState(
    this.name,
    this.age.toString(),
    this.gender.enum,
    metric = this.metric,
    weightUnit = if (weightUnit.isBlank()) 0 else weightUnits.indexOf(this.weightUnit),
    heightUnit = if (heightUnit.isBlank()) 0 else heightUnits.indexOf(this.heightUnit),
    actionsEnabled = this.isValid
)

val User.isValid get() = this.name.isNotBlank() && this.age > 0 && metric.isNotEmpty()