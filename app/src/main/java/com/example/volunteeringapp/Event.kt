package com.example.volunteeringapp

import android.location.Address
import java.util.Date

data class Event (
    val posterUid: String,
    val title: String,
    val description: String,
    val capacityNum: Int,
    val enrollmentNum: Int,
    val address: Address,
    val startDateTime: Date,
    val endDateTime: Date,
    val categories: List<String>
)