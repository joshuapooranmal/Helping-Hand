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

class LoginActivity : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginBtn: Button
    private lateinit var progressBar: ProgressBar
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailET = findViewById(R.id.email)
        passwordET = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        progressBar.visibility = View.VISIBLE

        val email : String = emailET.text.toString()
        val password : String = passwordET.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter an email!", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter a password!", Toast.LENGTH_LONG).show()
            return
        }

        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.login_success_string), Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@LoginActivity, ListActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, getString(R.string.login_failed_string), Toast.LENGTH_LONG).show()
                }
            }
    }
}