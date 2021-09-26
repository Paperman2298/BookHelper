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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    lateinit var name : EditText
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var confirmedPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.registerNameEditText)
        email = findViewById(R.id.registerEmailEditText)
        password = findViewById(R.id.registerPasswordEditText)
        confirmedPassword = findViewById(R.id.registerConfirmPasswordEditText)
    }

    fun onRegisterClicked(view: View?){
        //Create firebase user
        if(name.text.toString().isEmpty()){
            Toast.makeText(this, "¡Campo nombre vacio!", Toast.LENGTH_SHORT).show()
        }else if(email.text.toString().isEmpty()){
            Toast.makeText(this, "¡Campo correo vacio!", Toast.LENGTH_SHORT).show()
        }else if (password.text.toString().isEmpty()){
            Toast.makeText(this, "¡Campo contraseña vacio!", Toast.LENGTH_SHORT).show()
        }else if (confirmedPassword.text.toString().isEmpty()){
            Toast.makeText(this, "¡Campo confirmar contraseña vacio!", Toast.LENGTH_SHORT).show()
        }else{
            if(password.text.toString() == confirmedPassword.text.toString()){
                Firebase.auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this){
                        // Nos regresa un objeto "it" que indica si fue exitoso o no el registro
                        if (it.isSuccessful){
                            val data = hashMapOf(
                                "name" to name.text.toString(),
                                "email" to email.text.toString(),
                                "password" to password.text.toString(),
                                "books" to ArrayList<String>()
                            )
                            Firebase.firestore.collection("users")
                                .add(data)
                                .addOnSuccessListener { documentReference ->
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "¡Error!", Toast.LENGTH_SHORT).show()
                                }
                            Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
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
    }


    fun onLoginClicked(view: View?){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}