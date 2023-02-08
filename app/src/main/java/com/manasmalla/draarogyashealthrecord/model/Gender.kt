package com.manasmalla.draarogyashealthrecord.model


enum class Gender { Male, Female, Other }

val String.enum: Gender
    get() = when (this) {
        Gender.Male.name -> Gender.Male
        Gender.Female.name -> Gender.Female
        else -> Gender.Other
    }
