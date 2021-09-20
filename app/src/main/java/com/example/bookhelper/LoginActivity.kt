package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.util.Log
import android.widget.Toast
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
        if(email.text.toString().isEmpty()){
            Toast.makeText(this, "¡Campo correo vacio!", Toast.LENGTH_SHORT).show()
        }else if (password.text.toString().isEmpty()) {
            Toast.makeText(this, "¡Campo contraseña vacio!", Toast.LENGTH_SHORT).show()
        }else{
            Firebase.auth.signInWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            ).addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this, "¡Correo y contraseña inválido!", Toast.LENGTH_SHORT).show()
                //Log.e("FIREBASE", "Login fracaso: ${it.exception?.message}")
            }
        }
        //Firebase




    }

    fun onRegisterClicked(view : View?){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}