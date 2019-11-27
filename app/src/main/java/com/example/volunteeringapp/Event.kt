package com.example.volunteeringapp

import java.util.Date

data class Event (
    val posterUid: String = "",
    val eventid: String = "",
    val title: String = "",
    val description: String = "",
    val capacityNum: Int = 0,
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val startDateTime: Date = Date(),
    val endDateTime: Date = Date(),
    val registeredUsers: ArrayList<String> = ArrayList(),
    val savedUsers: ArrayList<String> = ArrayList()
)