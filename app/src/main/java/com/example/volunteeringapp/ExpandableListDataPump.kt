package com.example.volunteeringapp

import kotlin.collections.ArrayList

object ExpandableListDataPump {
    val data: ArrayList<Event>
        get() {
            val expandableListDetail = ArrayList<Event>()

            for(i in 1..10) {
                val posterUid = "ID $i"
                val eventId = "EventID $i"
                val title = "Title $i"
                val description = "Description $i"
                val capacityNum = 99 + i
                val street = i.toString() + "Street"
                val city = i.toString() + "Street"
                val state = "MD"
                val postalCode = (20000 + i).toString()
                val startDateTime = ListActivity.FORMAT.parse("0000-00-00 00:00")!!
                val endDateTime = ListActivity.FORMAT.parse("0000-00-00 00:00")!!
                val registeredUsers = ArrayList<String>()

                val toInsert = Event(posterUid, eventId, title, description, capacityNum, street, city,
                    state, postalCode, startDateTime, endDateTime, registeredUsers)

                expandableListDetail.add(toInsert)
            }

            return expandableListDetail
        }
}
