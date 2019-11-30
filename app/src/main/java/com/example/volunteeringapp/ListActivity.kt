package com.example.volunteeringapp

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.ExpandableListView
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

class ListActivity : AppCompatActivity() {

    lateinit var expandableListView: ExpandableListView
    lateinit var createEventButton: Button
    internal lateinit var expandableListDetail: MutableList<Event>

    internal lateinit var databaseEvents: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

        databaseEvents = FirebaseDatabase.getInstance().getReference("events")

        expandableListView = findViewById(R.id.expandableListView)
        createEventButton = findViewById(R.id.createEventButton)
        createEventButton.setOnClickListener {
            val addPostIntent = Intent(this, CreateEventActivity::class.java)
            startActivityForResult(addPostIntent, ADD_POST_REQUEST)
        }

        expandableListDetail = ArrayList()
    }

    override fun onStart() {
        super.onStart()
        databaseEvents.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Clearing the previous event list
                expandableListDetail.clear()

                // Iterating through all the event nodes
                for (postSnapshot in dataSnapshot.children) {
                    // Getting event
                    val event = postSnapshot.getValue<Event>(Event::class.java) as Event

                    when(MENU_CURRENT) {
                        MENU_ALL_EVENTS -> {
                            // Adding any event to the list
                            expandableListDetail.add(event)
                        }
                        MENU_CREATED_EVENTS -> {
                            // Adding created events unique to user
                            if (event.posterUid == intent.getStringExtra(LoginActivity.UserID))
                                expandableListDetail.add(event)
                        }
                        MENU_SIGNED_UP_EVENTS -> {
                            // Adding signed up events unique to user
                            if (event.registeredUsers.contains(intent?.getStringExtra(LoginActivity.UserID)))
                                expandableListDetail.add(event)
                        }
                        MENU_SAVED_EVENTS -> {
                            // Adding saved events unique to user
                            if (event.savedUsers.contains(intent?.getStringExtra(LoginActivity.UserID)))
                                expandableListDetail.add(event)
                        }
                    }
                }

                // Creating adapter using CustomExpandableList
                val expandableListAdapter = CustomExpandableListAdapter(this@ListActivity, expandableListDetail)
                // Attaching adapter to expandableListView
                expandableListView.setAdapter(expandableListAdapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_POST_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null)
                addEvent(data)
        }
    }

    private fun addEvent(data: Intent) {
        val title = data.getStringExtra(CreateEventActivity.TITLE)
        val description = data.getStringExtra(CreateEventActivity.DESCRIPTION)
        val capacityNum = data.getIntExtra(CreateEventActivity.CAPACITY, 0)
        val street = data.getStringExtra(CreateEventActivity.STREET)
        val city = data.getStringExtra(CreateEventActivity.CITY)
        val state = data.getStringExtra(CreateEventActivity.STATE)
        val postalCode = data.getStringExtra(CreateEventActivity.POSTALCODE)

        var startDateTime: Date
        var endDateTime: Date
        try {
            startDateTime = FORMAT.parse(data.getStringExtra(CreateEventActivity.START_DATETIME)!!)!!
            endDateTime = FORMAT.parse(data.getStringExtra(CreateEventActivity.END_DATETIME)!!)!!
        } catch (e: ParseException) {
            startDateTime = Date()
            endDateTime = Date()
        }

        /* Getting a unique id using push().getKey() method
        it will create a unique id and we will use it as the Primary Key for our event */
        val eventID = databaseEvents.push().key

        // Getting the user id of the user currently logged in
        val userID = intent.getStringExtra(LoginActivity.UserID)

        // Creating an Event Object
        val event = Event(userID!!, eventID!!, title!!, description!!, capacityNum,  street!!, city!!, state!!, postalCode!!, startDateTime, endDateTime, ArrayList(), ArrayList())

        // Saving the Event
        databaseEvents.child(eventID).setValue(event)

        // Displaying a success toast
        Toast.makeText(this, "Event added", Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_ALL_EVENTS, Menu.NONE, "All Events")
        menu.add(Menu.NONE, MENU_CREATED_EVENTS, Menu.NONE, "My Created Events")
        menu.add(Menu.NONE, MENU_SIGNED_UP_EVENTS, Menu.NONE, "My Signed Up Events")
        menu.add(Menu.NONE, MENU_SAVED_EVENTS, Menu.NONE, "My Saved Events")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_ALL_EVENTS -> {
                MENU_CURRENT = MENU_ALL_EVENTS
                onStart()
                return true
            }
            MENU_CREATED_EVENTS -> {
                MENU_CURRENT = MENU_CREATED_EVENTS
                onStart()
                return true
            }
            MENU_SIGNED_UP_EVENTS -> {
                MENU_CURRENT = MENU_SIGNED_UP_EVENTS
                onStart()
                return true
            }
            MENU_SAVED_EVENTS -> {
                MENU_CURRENT = MENU_SAVED_EVENTS
                onStart()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val ADD_POST_REQUEST = 0
        // ID for menu items
        private val MENU_ALL_EVENTS = Menu.FIRST
        private val MENU_CREATED_EVENTS = Menu.FIRST + 1
        private val MENU_SIGNED_UP_EVENTS = Menu.FIRST + 2
        private val MENU_SAVED_EVENTS = Menu.FIRST + 3

        private var MENU_CURRENT = MENU_ALL_EVENTS

        val FORMAT = SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.US)
    }
}
