package com.example.volunteeringapp

import android.location.Address
import java.util.Date

data class Event (
    val posterUid: String,
    val title: String,
    val description: String,
    val capacityNum: Int,
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val startDateTime: Date,
    val endDateTime: Date,
    val registeredUsers: ArrayList<String>
)