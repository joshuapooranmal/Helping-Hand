package com.example.volunteeringapp

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CustomExpandableListAdapter(
    private val context: Context,
    internal var expandableListDetail: List<Event>
) : BaseExpandableListAdapter() {

    private var auth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var geoCoder: Geocoder = Geocoder(context)

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
            listViewChildItem = layoutInflater.inflate(R.layout.list_item, null)
        }

        val textViewDescription = listViewChildItem?.findViewById<View>(R.id.description) as TextView
        val textViewAddress = listViewChildItem.findViewById<View>(R.id.address) as TextView
        val textViewStartDate = listViewChildItem.findViewById<View>(R.id.start_date) as TextView
        val textViewEndDate = listViewChildItem.findViewById<View>(R.id.end_date) as TextView
        val buttonSignUp = listViewChildItem.findViewById<View>(R.id.signUp) as Button
        val buttonDrop = listViewChildItem.findViewById<View>(R.id.drop) as Button
        val textViewCapacityFull = listViewChildItem.findViewById<View>(R.id.capacityFull) as TextView
        val checkBoxSaved = listViewChildItem.findViewById<View>(R.id.savedCheckBox) as CheckBox

        val event = getChild(listPosition, expandedListPosition) as Event

        textViewDescription.text = event.description

        val fullAdress = "${event.street}\n${event.city}, ${event.state} ${event.postalCode}"
        textViewAddress.text = fullAdress

        val sda = event.startDateTime.toString().split(" ")
        val eda = event.endDateTime.toString().split(" ")
        textViewStartDate.text =
            "Start\nDate: ${sda[0]} ${sda[1]} ${sda[2]}, ${sda[5]}\nTime: ${timeHelper(sda[3])} ${sda[4]}"
        textViewEndDate.text =
            "End\nDate: ${eda[0]} ${eda[1]} ${eda[2]}, ${eda[5]}\nTime: ${timeHelper(eda[3])} ${eda[4]}"

        if (event.registeredUsers.size == event.capacityNum) {
            buttonSignUp.visibility = View.GONE

            if (event.registeredUsers.contains(auth?.currentUser?.uid)) {
                buttonDrop.visibility = View.VISIBLE
                textViewCapacityFull.visibility = View.GONE
            } else {
                buttonDrop.visibility = View.GONE
                textViewCapacityFull.visibility = View.VISIBLE
            }
        } else {
            textViewCapacityFull.visibility = View.GONE

            if (event.registeredUsers.contains(auth?.currentUser?.uid)) {
                buttonDrop.visibility = View.VISIBLE
                buttonSignUp.visibility = View.GONE
            } else {
                buttonSignUp.visibility = View.VISIBLE
                buttonDrop.visibility = View.GONE
            }
        }

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

        if (event.savedUsers.contains(auth!!.currentUser!!.uid)) {
            checkBoxSaved.isChecked = true

            checkBoxSaved.setOnCheckedChangeListener { buttonView, isChecked ->
                if (!isChecked) {
                    event.savedUsers.remove(auth!!.currentUser!!.uid)

                    val db = FirebaseDatabase.getInstance().getReference("events").child(event.eventid)
                    val newEvent = Event(
                        event.posterUid,
                        event.eventid,
                        event.title,
                        event.description,
                        event.capacityNum,
                        event.street,
                        event.city,
                        event.state,
                        event.postalCode,
                        event.startDateTime,
                        event.endDateTime,
                        event.registeredUsers,
                        event.savedUsers
                    )

                    Toast.makeText(context, "Event removed from saved", Toast.LENGTH_LONG).show()

                    db.setValue(newEvent)
                }
            }
        } else {
            checkBoxSaved.isChecked = false

            checkBoxSaved.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    event.savedUsers.add(auth!!.currentUser!!.uid)

                    val db = FirebaseDatabase.getInstance().getReference("events").child(event.eventid)
                    val newEvent = Event(
                        event.posterUid,
                        event.eventid,
                        event.title,
                        event.description,
                        event.capacityNum,
                        event.street,
                        event.city,
                        event.state,
                        event.postalCode,
                        event.startDateTime,
                        event.endDateTime,
                        event.registeredUsers,
                        event.savedUsers
                    )

                    Toast.makeText(context, "Event saved", Toast.LENGTH_LONG).show()

                    db.setValue(newEvent)
                }
            }
        }

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
            listViewGroupItem = layoutInflater.inflate(R.layout.list_group, null)
        }
        val textViewTitle = listViewGroupItem?.findViewById<View>(R.id.title) as TextView
        val textViewSignUpCount = listViewGroupItem.findViewById<View>(R.id.signUpCount) as TextView
        val textViewDistance = listViewGroupItem.findViewById<View>(R.id.milesAway) as TextView

        val event = getGroup(listPosition) as Event

        val fullAdress = "${event.street}\n${event.city}, ${event.state} ${event.postalCode}"
        val address = geoCoder.getFromLocationName(fullAdress, 5)
        val location = address.get(0)

        textViewDistance.text = "${location.latitude} ${location.longitude}"

        textViewTitle.text = event.title
        textViewSignUpCount.text = "Enrolled: ${event.registeredUsers.size}/${event.capacityNum}"

        return listViewGroupItem
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    private fun timeHelper (t: String): String {
        val hour = t.substring(0, 2)
        val min = t.substring(3, 5)

        if(hour.toInt() == 12)
            return "${hour}:${min} PM"
        else if(hour.toInt() == 0)
            return "12:${min} AM"
        else if(hour.toInt() > 12)
            return "${(hour.toInt() - 12)}:${min} PM"
        else
            return "${hour}:${min} AM"
    }
}