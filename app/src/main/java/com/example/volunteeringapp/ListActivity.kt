package com.example.volunteeringapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.ExpandableListView
import android.widget.ExpandableListAdapter
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ListActivity : AppCompatActivity() {

    lateinit var expandableListView: ExpandableListView
    lateinit var createEventButton: Button
    lateinit var expandableListAdapter: ExpandableListAdapter
    lateinit var expandableListDetail: ArrayList<Event>

    //internal lateinit var events: MutableList<Event>
    internal lateinit var databaseEvents: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list)

        databaseEvents = FirebaseDatabase.getInstance().getReference("events")
        //events = ArrayList()

        createEventButton = findViewById(R.id.createEventButton) as Button
        expandableListView = findViewById(R.id.expandableListView) as ExpandableListView

        createEventButton.setOnClickListener {
            Toast.makeText(applicationContext, "Launching CreateEventActivity class!", Toast.LENGTH_LONG).show()
            val addPostIntent = Intent(this, CreateEventActivity::class.java)
            startActivityForResult(addPostIntent, ADD_POST_REQUEST)
        }

        expandableListDetail = ExpandableListDataPump.data
        expandableListAdapter =
            CustomExpandableListAdapter(this, expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter)

        expandableListView.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                expandableListDetail[groupPosition].title + " Expanded.",
                Toast.LENGTH_SHORT
            ).show()
        }

        expandableListView.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                expandableListDetail[groupPosition].title + " Collapsed.",
                Toast.LENGTH_SHORT
            ).show()
        }

        expandableListView.setOnChildClickListener(object :
            ExpandableListView.OnChildClickListener {
            override fun onChildClick(
                parent: ExpandableListView, v: View,
                groupPosition: Int, childPosition: Int, id: Long
            ): Boolean {
                Toast.makeText(
                    applicationContext,
                    expandableListDetail[groupPosition].title
                            + " -> "
                            + expandableListDetail[groupPosition]!!.description,
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        })
    }

    /*override fun onStart() {
        super.onStart()
        databaseEvents.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //clearing the previous event list
                events.clear()

                //iterating through all the event nodes
                for (postSnapshot in dataSnapshot.children) {
                    //getting author
                    val event = postSnapshot.getValue<Event>(Event::class.java)
                    //adding event to the list
                    events.add(event!!)
                }

                //creating adapter using CustomExpandableList
                //TODO: Pass in the events arrayList substituting expandableListTitle or expandableListDetail (figure this out) to CustomExpandableList()
                val eventAdapter = CustomExpandableListAdapter(this@ListActivity, expandableListTitle, expandableListDetail)
                //attaching adapter to the expandablelistview
                expandableListView.setAdapter(eventAdapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }*/

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
        val enrollmentNum = data.getIntExtra(CreateEventActivity.ENROLLMENT, 0)
        val street = data.getStringExtra(CreateEventActivity.STREET)
        val city = data.getStringExtra(CreateEventActivity.CITY)
        val state = data.getStringExtra(CreateEventActivity.STATE)
        val postalCode = data.getStringExtra(CreateEventActivity.POSTALCODE)

        var startDateTime: Date
        var endDateTime: Date
        try {
            startDateTime = FORMAT.parse(data.getStringExtra(CreateEventActivity.START_DATETIME))
            endDateTime = FORMAT.parse(data.getStringExtra(CreateEventActivity.END_DATETIME))
        } catch (e: ParseException) {
            startDateTime = Date()
            endDateTime = Date()
        }

        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our event
        val eventID = databaseEvents.push().key
        val userID = intent.getStringExtra(LoginActivity.UserID)

        //creating an Event Object
        val event = Event(userID!!, title, description, capacityNum,  street, city, state, postalCode, startDateTime, endDateTime, ArrayList())

        //Saving the Evemt
        databaseEvents.child(eventID!!).setValue(event)

        // TODO: Create an add method in CustomExpandableList that adds a data view for the event in the list view?
        //expandableListAdapter.add(event)

        //displaying a success toast
        Toast.makeText(this, "Event added", Toast.LENGTH_LONG).show()
        //Checking to see if Event data is retrieved correctly
        Toast.makeText(this, "Title: ${title} \n" +
                "Description: ${description} \n" +
                "Capacity: ${enrollmentNum}/${capacityNum} \n" +
                "Address: ${street} ${city} ${state} ${postalCode} \n" +
                "Start Date: ${startDateTime} \n" +
                "End Date: ${endDateTime} \n" +
                "Categories: none", Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_MY_EVENTS, Menu.NONE, "View My Events")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_MY_EVENTS -> {
                val myEventsIntent = Intent(this, MyEventsActivity::class.java)
                startActivity(myEventsIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val ADD_POST_REQUEST = 0
        // ID for menu item
        private val MENU_MY_EVENTS = Menu.FIRST

        val FORMAT = SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.US)
    }
}
