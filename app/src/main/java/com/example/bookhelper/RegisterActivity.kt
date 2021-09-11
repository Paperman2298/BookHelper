package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var confirmedPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.registerEmailEditText)
        password = findViewById(R.id.registerPasswordEditText)
        confirmedPassword = findViewById(R.id.registerConfirmPasswordEditText)
    }

    fun onRegisterClicked(view: View?){
        //Create firebase user

        // Random comment
    }

    fun onLoginClicked(view: View?){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}