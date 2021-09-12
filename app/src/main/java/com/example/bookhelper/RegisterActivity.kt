package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        Firebase.auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this){
                // Nos regresa un objeto "it" que indica si fue exitoso o no el registro
                if (it.isSuccessful){
                    Log.d("FIREBASE", "Registro exitoso")
                }else {
                    Log.d("FIREBASE", "Registro fracasó: ${it.exception?.message}")
                }
            }
    }

    fun onLoginClicked(view: View?){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}