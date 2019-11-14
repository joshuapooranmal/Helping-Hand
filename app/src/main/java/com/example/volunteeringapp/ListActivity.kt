package com.example.volunteeringapp

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_list.*
import org.w3c.dom.Text


class ListActivity : AppCompatActivity() {

    lateinit var expandableListView: ExpandableListView
    lateinit var createEventButton: Button
    lateinit var expandableListAdapter: ExpandableListAdapter
    lateinit var expandableListTitle: List<String>
    lateinit var expandableListDetail: HashMap<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list)

        createEventButton = findViewById(R.id.createEventButton)
        expandableListView = findViewById(R.id.expandableListView)

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
}
