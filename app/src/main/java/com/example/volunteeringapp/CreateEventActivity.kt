package com.example.volunteeringapp

import java.util.Date
import java.util.Calendar

import android.app.Activity
import android.os.Bundle
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_event.*

class CreateEventActivity : Activity() {
    private var titleText: EditText? = null
    private var descriptionText: EditText? = null
    private var capacityNum: EditText? = null
    private var streetText: EditText? = null
    private var cityText: EditText? = null
    private var stateText: EditText? = null
    private var postalCodeNum: EditText? = null
    private var startDateView: TextView? = null
    private var startTimeView: TextView? = null
    private var endDateView: TextView? = null
    private var endTimeView: TextView? = null
    private var startDatePickerButton: Button? = null
    private var startTimePickerButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        titleText = findViewById<View>(R.id.title) as EditText
        descriptionText = findViewById<View>(R.id.description) as EditText
        capacityNum = findViewById<View>(R.id.capacity) as EditText
        streetText = findViewById<View>(R.id.street) as EditText
        cityText = findViewById<View>(R.id.city) as EditText
        stateText = findViewById<View>(R.id.state) as EditText
        postalCodeNum = findViewById<View>(R.id.postalCode) as EditText
        startDateView = findViewById<View>(R.id.startDate) as TextView
        startTimeView = findViewById<View>(R.id.startTime) as TextView
        endDateView = findViewById<View>(R.id.endDate) as TextView
        endTimeView = findViewById<View>(R.id.endTime) as TextView
        startDatePickerButton = findViewById<View>(R.id.startDate_picker_button) as Button
        startTimePickerButton = findViewById<View>(R.id.startTime_picker_button) as Button

        setDefaultDateTime()

        val cancelBtn = findViewById<View>(R.id.btnCancel) as Button
        cancelBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        val postBtn = findViewById<View>(R.id.btnPost) as Button
        postBtn.setOnClickListener {

            if(titleText?.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a title!", Toast.LENGTH_LONG).show()
            } else if(descriptionText?.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a description!", Toast.LENGTH_LONG).show()
            } else if(capacityNum?.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a capacity!", Toast.LENGTH_LONG).show()
            } else if(streetText?.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a street!", Toast.LENGTH_LONG).show()
            } else if(cityText?.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a city!", Toast.LENGTH_LONG).show()
            } else if(stateText?.text!!.isEmpty() || stateText?.text!!.length != 2) {
                Toast.makeText(applicationContext, "Please enter a valid state!", Toast.LENGTH_LONG).show()
            } else if(postalCodeNum?.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a postal code!", Toast.LENGTH_LONG).show()
            } else {
                val postIntent = Intent()
                packageIntent(postIntent, titleText?.text.toString(), descriptionText?.text.toString(),
                    capacityNum?.text.toString(),
                    streetText?.text.toString(), cityText?.text.toString(),
                    stateText?.text.toString(), postalCodeNum?.text.toString(),
                    startDateView?.text.toString() + " " + startTimeView?.text.toString(),
                    endDateView?.text.toString() + " " + endTimeView?.text.toString()
                )
                setResult(RESULT_OK, postIntent)
                finish()
            }
        }

    }

    private fun setDefaultDateTime() {
        val c = Calendar.getInstance()
        c.time = Date()

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH))

        startDateView!!.text = dateString
        endDateView!!.text = dateString

        setTimeString(0, 0, Calendar.PM)

        startTimeView!!.text = timeString
        endTimeView!!.text = timeString
    }

    fun showDatePickerDialog(v: View) {
        val year: Int
        val month: Int
        val day: Int
        if (v == startDatePickerButton) {
            val str = startDateView!!.text.toString().split("-")
            year = str[0].toInt()
            month = str[1].toInt() - 1
            day = str[2].toInt()
        } else {
            val str = endDateView!!.text.toString().split("-")
            year = str[0].toInt()
            month = str[1].toInt() - 1
            day = str[2].toInt()
        }

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view, year, monthOfYear, dayOfMonth ->
            setDateString(year, monthOfYear, dayOfMonth)
            if (v == startDatePickerButton)
                startDateView!!.text = dateString
            else
                endDateView!!.text = dateString
        }, year, month, day)
        dpd.show()
    }

    fun showTimePickerDialog(v: View) {
        val hour: Int
        val min: Int
        if (v == startTimePickerButton) {
            val str = startTimeView!!.text.toString().split(":")
            hour = str[0].toInt()
            min = str[1].substring(0, 2).toInt()
        } else {
            val str = endTimeView!!.text.toString().split(":")
            hour = str[0].toInt()
            min = str[1].substring(0, 2).toInt()
        }

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {view, hourOfDay, minute ->
            val datetime = Calendar.getInstance()
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            datetime.set(Calendar.MINUTE, minute)

            setTimeString(datetime.get(Calendar.HOUR), minute, datetime.get(Calendar.AM_PM))
            if (v == startTimePickerButton)
                startTimeView!!.text = timeString
            else
                endTimeView!!.text = timeString
        }, hour, min, false)
        tpd.show()
    }

    companion object {
        val TITLE = "title"
        val DESCRIPTION = "description"
        val CAPACITY = "capacity number"
        val STREET = "street"
        val CITY = "city"
        val STATE = "state"
        val POSTALCODE = "postal code"
        val START_DATETIME = "start datetime"
        val END_DATETIME = "end datetime"
        private var timeString: String? = null
        private var dateString: String? = null

        private fun setDateString(year: Int, monthOfYear: Int, dayOfMonth: Int) {
            var monthOfYear = monthOfYear

            // Increment monthOfYear for Calendar/Date -> Time Format setting
            monthOfYear++
            var mon = "" + monthOfYear
            var day = "" + dayOfMonth

            if (monthOfYear < 10)
                mon = "0$monthOfYear"
            if (dayOfMonth < 10)
                day = "0$dayOfMonth"

            dateString = "$year-$mon-$day"
        }

        private fun setTimeString(hourOfDay: Int, minute: Int, typeOfHour: Int) {
            var hour = "" + hourOfDay
            var min = "" + minute
            var type = ""

            if (hourOfDay == 0) {
                hour = "12"
            }
            if (minute < 10)
                min = "0$minute"
            if (typeOfHour == Calendar.AM) {
                type = "AM"
            } else {
                type = "PM"
            }

            timeString = "$hour:$min $type"
        }

        fun packageIntent(intent: Intent, title: String, description: String, capacityNum: String,
                          street: String, city: String, state: String, postalCode: String,
                          startDateTime: String, endDateTime: String) {
            intent.putExtra(TITLE, title)
            intent.putExtra(DESCRIPTION, description)
            intent.putExtra(CAPACITY, capacityNum.toInt())
            intent.putExtra(STREET, street)
            intent.putExtra(CITY, city)
            intent.putExtra(STATE, state)
            intent.putExtra(POSTALCODE, postalCode)
            intent.putExtra(START_DATETIME, startDateTime)
            intent.putExtra(END_DATETIME, endDateTime)
        }
    }
}