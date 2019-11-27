package com.example.volunteeringapp

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CustomExpandableListAdapter(
    private val context: Context,
    internal var expandableListDetail: List<Event>
) : BaseExpandableListAdapter() {

    private var auth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.expandableListDetail[listPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int, expandedListPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var listViewChildItem = convertView
        if (listViewChildItem == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            listViewChildItem = layoutInflater.inflate(R.layout.list_item, null, true)
        }

        val textViewDescription = listViewChildItem?.findViewById<View>(R.id.description) as TextView

        val event = getChild(listPosition, expandedListPosition) as Event
        textViewDescription.text = event.description

        return listViewChildItem
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return 1
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListDetail[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.expandableListDetail.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup): View {
        var listViewGroupItem = convertView
        if (listViewGroupItem == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            listViewGroupItem = layoutInflater.inflate(R.layout.list_group, null, true)
        }
        val textViewTitle = listViewGroupItem?.findViewById<View>(R.id.title) as TextView
        val textViewSignUpCount = listViewGroupItem.findViewById<View>(R.id.signUpCount) as TextView
        val buttonSignUp = listViewGroupItem.findViewById<View>(R.id.signUp) as Button
        val buttonDrop = listViewGroupItem.findViewById<View>(R.id.drop) as Button

        val event = getGroup(listPosition) as Event
        textViewTitle.setTypeface(null, Typeface.BOLD)
        textViewTitle.text = event.title
        textViewSignUpCount.text = "Enrolled: ${event.registeredUsers.size}/${event.capacityNum}"

        buttonSignUp.setOnClickListener {
            event.registeredUsers.add(auth!!.currentUser!!.uid)

            val updatedRegisteredUsers = event.registeredUsers
            val db = FirebaseDatabase.getInstance().getReference("events").child(event.eventid)
            val newEvent = Event(event.posterUid, event.eventid, event.title, event.description, event.capacityNum,
                event.street, event.city, event.state, event.postalCode, event.startDateTime, event.endDateTime,
                updatedRegisteredUsers, event.savedUsers)

            db.setValue(newEvent)
        }

        buttonDrop.setOnClickListener {
            event.registeredUsers.remove(auth!!.currentUser!!.uid)

            val updatedRegisteredUsers = event.registeredUsers
            val db = FirebaseDatabase.getInstance().getReference("events").child(event.eventid)
            val newEvent = Event(event.posterUid, event.eventid, event.title, event.description, event.capacityNum,
                event.street, event.city, event.state, event.postalCode, event.startDateTime, event.endDateTime,
                updatedRegisteredUsers, event.savedUsers)

            db.setValue(newEvent)
        }

        // TODO add save checkbox functionality here

        // TODO add rating spinner functionality here

        if (event.registeredUsers.contains(auth?.currentUser?.uid)) {
            buttonDrop.visibility = View.VISIBLE
        } else {
            if (event.registeredUsers.size == event.capacityNum) {
                val textViewCapacityFull = listViewGroupItem.findViewById<View>(R.id.capacityFull) as TextView
                textViewCapacityFull.visibility = View.VISIBLE
            } else {
                buttonSignUp.visibility = View.VISIBLE
            }
        }

        return listViewGroupItem
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}