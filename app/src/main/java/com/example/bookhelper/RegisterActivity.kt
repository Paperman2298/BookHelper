package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        if(password.text.toString() == confirmedPassword.text.toString()){
            Firebase.auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this){
                    // Nos regresa un objeto "it" que indica si fue exitoso o no el registro
                    if (it.isSuccessful){
                        Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this, "¡Correo inválido!", Toast.LENGTH_SHORT).show()
                        //Log.d("FIREBASE", "Registro fracasó: ${it.exception?.message}")
                    }
                }
        }else{
            Toast.makeText(this, "¡La contraseña de verificación no coincide!", Toast.LENGTH_SHORT).show()
            //Log.d("FIREBASE", "La contraseña no es la misma")
        }
    }

    
    fun onLoginClicked(view: View?){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}