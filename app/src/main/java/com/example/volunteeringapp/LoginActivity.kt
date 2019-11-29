package com.example.volunteeringapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var notRegistered: TextView
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailET = findViewById(R.id.email)
        passwordET = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)
        notRegistered = findViewById(R.id.not_registered)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener { loginUser() }

        notRegistered.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
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
                    val user = auth!!.currentUser
                    val uid = user!!.uid
                    val intent = Intent(this@LoginActivity, ListActivity::class.java)
                    intent.putExtra(UserID, uid)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.login_failed_string), Toast.LENGTH_LONG).show()
                }
            }
    }

    companion object {
        val UserID = "com.example.volunteeringapp.UID"
    }
}