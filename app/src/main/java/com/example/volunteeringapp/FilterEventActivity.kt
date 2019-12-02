package com.example.volunteeringapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import java.util.*

class FilterEventActivity : Activity() {
    private var FromDateView: TextView? = null
    private var ToDateView: TextView? = null
    private var FromTimeView: TextView? = null
    private var ToTimeView: TextView? = null
    private var FromDateBtn: Button? = null
    private var FromTimeBtn: Button? = null
    private var milesEditTxt: EditText? = null
    private var milesCheckBox: CheckBox? = null
    private var dateCheckBox: CheckBox? = null
    private var timeCheckBox: CheckBox? = null
    private var capacityCheckBox: CheckBox? = null


    private lateinit var mPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPrefs = getPreferences(Context.MODE_PRIVATE)

        setContentView(R.layout.activity_filter)

        FromDateView = findViewById<View>(R.id.txtFromDate) as TextView
        ToDateView = findViewById<View>(R.id.txtToDate) as TextView
        FromTimeView = findViewById<View>(R.id.txtFromTime) as TextView
        ToTimeView = findViewById<View>(R.id.txtToTime) as TextView
        FromDateBtn = findViewById<View>(R.id.btnFromDate) as Button
        FromTimeBtn = findViewById<View>(R.id.btnFromTime) as Button
        milesEditTxt = findViewById(R.id.milesEditTxt) as EditText
        milesCheckBox = findViewById(R.id.milesCheckBox) as CheckBox
        dateCheckBox = findViewById(R.id.dateCheckBox) as CheckBox
        timeCheckBox = findViewById(R.id.timeCheckBox) as CheckBox
        capacityCheckBox = findViewById(R.id.capacityCheckBox) as CheckBox


        if (intent.getBooleanExtra(ListActivity.CLEAR_PREFS_STR, false)) {
            val editor = mPrefs.edit()
            editor.clear()
            editor.apply()
        }

        setDefaultDateTime()
        val savedMiles = mPrefs.getFloat(MILES, -1F)
        if (savedMiles == -1F) {
            milesEditTxt!!.text.clear()
        } else {
            milesEditTxt?.setText(savedMiles.toString())
        }
        milesCheckBox?.isChecked = mPrefs.getBoolean(MILES_CHECK, false)
        dateCheckBox?.isChecked = mPrefs.getBoolean(DATE_CHECK, false)
        timeCheckBox?.isChecked = mPrefs.getBoolean(TIME_CHECK, false)
        capacityCheckBox?.isChecked = mPrefs.getBoolean(CAPACITY_CHECK, false)

        val cancelBtn = findViewById<View>(R.id.btnCancel) as Button
        cancelBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        val applyBtn = findViewById<View>(R.id.btnApply) as Button
        applyBtn.setOnClickListener {
            val editor = mPrefs.edit()
            editor.putString(FROM_DATE, FromDateView?.text.toString())
            editor.putString(TO_DATE, ToDateView?.text.toString())
            editor.putString(FROM_TIME, FromTimeView?.text.toString())
            editor.putString(TO_TIME, ToTimeView?.text.toString())
            editor.putFloat(MILES, milesEditTxt?.text.toString().toFloat())
            editor.putBoolean(MILES_CHECK, milesCheckBox!!.isChecked)
            editor.putBoolean(DATE_CHECK, dateCheckBox!!.isChecked)
            editor.putBoolean(TIME_CHECK, timeCheckBox!!.isChecked)
            editor.putBoolean(CAPACITY_CHECK, capacityCheckBox!!.isChecked)
            editor.apply()

            val applyIntent = Intent()
            packageIntent(
                applyIntent,
                FromDateView?.text.toString(),
                ToDateView?.text.toString(),
                FromTimeView?.text.toString(),
                ToTimeView?.text.toString(),
                milesEditTxt?.text.toString().toFloat(),
                milesCheckBox!!.isChecked,
                dateCheckBox!!.isChecked,
                timeCheckBox!!.isChecked,
                capacityCheckBox!!.isChecked
            )
            setResult(RESULT_OK, applyIntent)
            finish()
        }

        val clearBtn = findViewById<View>(R.id.btnClear) as Button
        clearBtn.setOnClickListener {
            val editor = mPrefs.edit()
            editor.clear()
            editor.apply()

            setDefaultDateTime()
            milesEditTxt!!.text.clear()
            milesCheckBox!!.isChecked = false
            capacityCheckBox!!.isChecked = false
            dateCheckBox!!.isChecked = false
            timeCheckBox!!.isChecked = false
            setResult(RESULT_OK, null)
            finish()
        }
    }

    private fun setDefaultDateTime() {
        val c = Calendar.getInstance()
        c.time = Date()

        setDateString(
            c.get(Calendar.YEAR), c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )

        FromDateView!!.text = mPrefs.getString(FROM_DATE, dateString)
        ToDateView!!.text = mPrefs.getString(TO_DATE, dateString)

        setTimeString(0, 0, Calendar.PM)

        FromTimeView!!.text = mPrefs.getString(FROM_TIME, timeString)
        ToTimeView!!.text = mPrefs.getString(TO_TIME, timeString)
    }

    fun showDatePickerDialog(v: View) {
        val year: Int
        val month: Int
        val day: Int
        if (v == FromDateBtn) {
            val str = FromDateView!!.text.toString().split("-")
            year = str[0].toInt()
            month = str[1].toInt() - 1
            day = str[2].toInt()
        } else {
            val str = ToDateView!!.text.toString().split("-")
            year = str[0].toInt()
            month = str[1].toInt() - 1
            day = str[2].toInt()
        }

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            setDateString(year, monthOfYear, dayOfMonth)
            if (v == FromDateBtn)
                FromDateView!!.text = dateString
            else
                ToDateView!!.text = dateString
        }, year, month, day)
        dpd.show()
    }

    fun showTimePickerDialog(v: View) {
        val hour: Int
        val min: Int
        if (v == FromTimeBtn) {
            val str = FromTimeView!!.text.toString().split(":")
            hour = str[0].toInt()
            min = str[1].substring(0, 2).toInt()
        } else {
            val str = ToTimeView!!.text.toString().split(":")
            hour = str[0].toInt()
            min = str[1].substring(0, 2).toInt()
        }

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val datetime = Calendar.getInstance()
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            datetime.set(Calendar.MINUTE, minute)

            setTimeString(datetime.get(Calendar.HOUR), minute, datetime.get(Calendar.AM_PM))
            if (v == FromTimeBtn)
                FromTimeView!!.text = timeString
            else
                ToTimeView!!.text = timeString
        }, hour, min, false)
        tpd.show()
    }

    companion object {
        val FROM_DATE = "from date"
        val TO_DATE = "to date"
        val FROM_TIME = "from time"
        val TO_TIME = "to time"
        val MILES = "miles"
        val MILES_CHECK = "miles check"
        val DATE_CHECK = "date check"
        val TIME_CHECK = "time check"
        val CAPACITY_CHECK = "capacity check"
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

        fun packageIntent(intent: Intent, fromDate: String, toDate: String, fromTime: String, toTime: String, miles: Float,
                          milesCheck: Boolean, dateCheck: Boolean, timeCheck: Boolean, capacityCheck: Boolean) {
            intent.putExtra(FROM_DATE, fromDate)
            intent.putExtra(TO_DATE, toDate)
            intent.putExtra(FROM_TIME, fromTime)
            intent.putExtra(TO_TIME, toTime)
            intent.putExtra(MILES, miles)
            intent.putExtra(MILES_CHECK, milesCheck)
            intent.putExtra(DATE_CHECK, dateCheck)
            intent.putExtra(TIME_CHECK, timeCheck)
            intent.putExtra(CAPACITY_CHECK, capacityCheck)
        }
    }
}