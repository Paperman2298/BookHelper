package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.loginEmailEditText)
        password = findViewById(R.id.loginPasswordEditText)
    }

    fun onLoginClicked(view: View?){
        //Firebase
        Firebase.auth.signInWithEmailAndPassword(
            email.text.toString(),
            password.text.toString()
        ).addOnCompleteListener(this){
            if(it.isSuccessful)
                Log.d("FIREBASE", "Login exitoso")
            else
                Log.e("FIREBASE", "Login fracasó: ${it.exception?.message}")
        }
    }

    fun onRegisterClicked(view : View?){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}