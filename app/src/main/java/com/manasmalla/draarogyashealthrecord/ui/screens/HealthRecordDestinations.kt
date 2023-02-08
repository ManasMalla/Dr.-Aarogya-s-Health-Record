package com.manasmalla.draarogyashealthrecord.ui.screens

sealed class HealthRecordDestinations{
    object SplashDestination : HealthRecordDestinations()

    object LoginDestination : HealthRecordDestinations()

    object HomeDestination : HealthRecordDestinations()

    object ManageProfileDestination : HealthRecordDestinations()

}
