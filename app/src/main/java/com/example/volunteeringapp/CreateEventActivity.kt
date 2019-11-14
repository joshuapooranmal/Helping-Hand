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

class CreateEventActivity : Activity() {

    private var titleText: EditText? = null
    private var descriptionText: EditText? = null
    private var capacityNum: EditText? = null
    private var enrollmentNum: EditText? = null
    private var streetText: EditText? = null
    private var cityText: EditText? = null
    private var stateText: EditText? = null
    private var postalCodeNum: EditText? = null
    private var startDateView: TextView? = null
    private var startTimeView: TextView? = null
    private var endDateView: TextView? = null
    private var endTimeView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        titleText = findViewById<View>(R.id.title) as EditText
        descriptionText = findViewById<View>(R.id.description) as EditText
        capacityNum = findViewById<View>(R.id.capacity) as EditText
        enrollmentNum = findViewById<View>(R.id.enrollment) as EditText
        streetText = findViewById<View>(R.id.street) as EditText
        cityText = findViewById<View>(R.id.city) as EditText
        stateText = findViewById<View>(R.id.state) as EditText
        postalCodeNum = findViewById<View>(R.id.postalCode) as EditText
        startDateView = findViewById<View>(R.id.startDate) as TextView
        startTimeView = findViewById<View>(R.id.startTime) as TextView
        endDateView = findViewById<View>(R.id.endDate) as TextView
        endTimeView = findViewById<View>(R.id.endTime) as TextView

        setDefaultDateTime()

        val startDatePickerButton = findViewById<View>(R.id.startDate_picker_button) as Button
        startDatePickerButton.setOnClickListener { showDatePickerDialog(startDateView as TextView) }

        val startTimePickerButton = findViewById<View>(R.id.startTime_picker_button) as Button
        startTimePickerButton.setOnClickListener { showTimePickerDialog(startTimeView as TextView) }

        val endDatePickerButton = findViewById<View>(R.id.endDate_picker_button) as Button
        endDatePickerButton.setOnClickListener { showDatePickerDialog(endDateView as TextView) }

        val endTimePickerButton = findViewById<View>(R.id.endTime_picker_button) as Button
        endTimePickerButton.setOnClickListener { showTimePickerDialog(endTimeView as TextView) }

        val cancelBtn = findViewById<View>(R.id.cancel) as Button
        cancelBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        val postBtn = findViewById<View>(R.id.post) as Button
        postBtn.setOnClickListener {
            val postIntent = Intent()
            packageIntent(postIntent, titleText?.text.toString(), descriptionText?.text.toString(),
                capacityNum?.text.toString(), enrollmentNum?.text.toString(),
                streetText?.text.toString(), cityText?.text.toString(),
                stateText?.text.toString(), postalCodeNum?.text.toString(),
                startDateView?.text.toString() + " " + startTimeView?.text.toString(),
                endDateView?.text.toString() + " " + endTimeView?.text.toString()
            )
            setResult(RESULT_OK, postIntent)
            finish()
        }

    }

    private fun setDefaultDateTime() {
        val c = Calendar.getInstance()
        c.time = Date()

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH))

        startDateView!!.text = dateString
        endDateView!!.text = dateString

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))

        startTimeView!!.text = timeString
        endTimeView!!.text = timeString
    }

    private fun showDatePickerDialog(dateView: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view, year, monthOfYear, dayOfMonth ->
            setDateString(year, monthOfYear, dayOfMonth)
            dateView!!.text = dateString
        }, year, month, day)
        dpd.show()
    }

    private fun showTimePickerDialog(timeView: TextView) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {view, hourOfDay, minute ->
            setTimeString(hourOfDay, minute)
            timeView!!.text = timeString
        }, hour, minute, true)
        tpd.show()
    }

    companion object {
        val TITLE = "title"
        val DESCRIPTION = "description"
        val CAPACITY = "capacity number"
        val ENROLLMENT = "enrollment number"
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

        private fun setTimeString(hourOfDay: Int, minute: Int) {
            var hour = "" + hourOfDay
            var min = "" + minute

            if (hourOfDay < 10)
                hour = "0$hourOfDay"
            if (minute < 10)
                min = "0$minute"

            timeString = "$hour:$min"
        }

        fun packageIntent(intent: Intent, title: String, description: String, capacityNum: String,
                          enrollmentNum: String, street: String, city: String, state: String, postalCode: String,
                          startDateTime: String, endDateTime: String) {
            intent.putExtra(TITLE, title)
            intent.putExtra(DESCRIPTION, description)
            intent.putExtra(CAPACITY, capacityNum.toInt())
            intent.putExtra(ENROLLMENT, enrollmentNum.toInt())
            intent.putExtra(STREET, street)
            intent.putExtra(CITY, city)
            intent.putExtra(STATE, state)
            intent.putExtra(POSTALCODE, postalCode)
            intent.putExtra(START_DATETIME, startDateTime)
            intent.putExtra(END_DATETIME, endDateTime)
        }
    }
}