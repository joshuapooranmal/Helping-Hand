package com.example.volunteeringapp

import android.location.Address
import java.util.*

data class Event (
    val posterUid: String,
    val title: String,
    val description: String,
    val maxCapacity: Int,
    val currentCapacity: Int,
    val address: Address,
    val startTime: Date,
    val endTime: Date,
    val categories: List<String>
)
