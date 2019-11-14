package com.example.volunteeringapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.location.Address
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.ExpandableListView
import android.widget.ExpandableListAdapter
import android.widget.*
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_list.*
import org.w3c.dom.Text

class ListActivity : AppCompatActivity() {

    lateinit var expandableListView: ExpandableListView
    lateinit var createEventButton: Button
    lateinit var expandableListAdapter: ExpandableListAdapter
    lateinit var expandableListTitle: List<String>
    lateinit var expandableListDetail: HashMap<String, List<String>>

    //internal lateinit var events: MutableList<Event>
    //internal lateinit var databaseEvents: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list)

        //databaseEvents = FirebaseDatabase.getInstance().getReference("events")
        //events = ArrayList()
        createEventButton = findViewById(R.id.createEventButton) as Button
        expandableListView = findViewById(R.id.expandableListView) as ExpandableListView

        createEventButton.setOnClickListener {
            Toast.makeText(applicationContext, "Launching CreateEventActivity Class!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@ListActivity, CreateEventActivity::class.java))
        }

        expandableListDetail = ExpandableListDataPump.data
        expandableListTitle = ArrayList<String>(expandableListDetail.keys)
        expandableListAdapter =
            CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter)
        expandableListView.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                expandableListTitle[groupPosition] + " List Expanded.",
                Toast.LENGTH_SHORT
            ).show()
        }

        expandableListView.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                expandableListTitle[groupPosition] + " List Collapsed.",
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
                    expandableListTitle[groupPosition]
                            + " -> "
                            + expandableListDetail[expandableListTitle[groupPosition]]!!.get(
                        childPosition
                    ), Toast.LENGTH_SHORT
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

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_POST_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null)
                addEvent(data)
        }
    }*/

    /*private fun addEvent(data: Intent) {
        val title = data.getStringExtra(PostingActivity.TITLE)
        val description = data.getStringExtra(PostingActivity.DESCRIPTION)
        val capacityNum = data.getIntExtra(PostingActivity.CAPACITY, 0)
        val enrollmentNum = data.getIntExtra(PostingActivity.ENROLLMENT, 0)

        // TODO: Figure how to create a proper address object that will contain the intent data for address
        val address = Address(Locale.US)
        val addressString = data.getStringExtra(PostingActivity.ADDRESS)

        var startDateTime: Date
        var endDateTime: Date
        try {
            startDateTime = FORMAT.parse(data.getStringExtra(PostingActivity.START_DATETIME))
            endDateTime = FORMAT.parse(data.getStringExtra(PostingActivity.END_DATETIME))
        } catch (e: ParseException) {
            startDateTime = Date()
            endDateTime = Date()
        }

        // TODO: Create several categories for events
        val categories = ArrayList<String>()

        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our event
        val id = databaseEvents.push().key

        //creating an Event Object
        val event = Event(id!!, title, description, capacityNum, enrollmentNum, address, startDateTime, endDateTime, categories)

        //Saving the Evemt
        databaseEvents.child(id).setValue(event)

        // TODO: Create an add method in CustomExpandableList that adds a data view for the event in the list view?
        expandableListAdapter.add(event)

        //displaying a success toast
        Toast.makeText(this, "Post added", Toast.LENGTH_LONG).show()
    }*/

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_POST, Menu.NONE, "Post new event")
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_POST -> {
                val addPostIntent = Intent(this, PostingActivity::class.java)
                startActivityForResult(addPostIntent, ADD_POST_REQUEST)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }*/

    /*companion object {
        private val ADD_POST_REQUEST = 0
        // ID for menu item
        private val MENU_POST = Menu.FIRST

        val FORMAT = SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.US)
    }*/
}
