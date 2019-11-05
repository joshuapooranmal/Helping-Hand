package com.example.volunteeringapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var registerBtn: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        emailET = findViewById(R.id.email)
        passwordET = findViewById(R.id.password)
        registerBtn = findViewById(R.id.register)
        progressBar = findViewById(R.id.progressBar)

        registerBtn.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {
        progressBar.visibility = View.VISIBLE

        val email : String = emailET.text.toString()
        val password : String = passwordET.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter an email!", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter a password!", Toast.LENGTH_LONG).show()
            return
        }

        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.register_success_string), Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, getString(R.string.register_failed_string), Toast.LENGTH_LONG).show()
                }
            }
    }
}