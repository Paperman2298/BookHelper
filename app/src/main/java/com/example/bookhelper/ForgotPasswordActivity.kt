package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var email : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        email = findViewById(R.id.forgotEmailEditText)

    }


    fun onSubmitClicked(view : View?){
        val emailForgot: String = email.text.toString().trim(){it <= ' '}
        if (emailForgot.isEmpty()){
            Toast.makeText(this, "Please enter a email address.",Toast.LENGTH_SHORT).show()
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailForgot)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Email sent successfully to rest your password", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    fun onLoginClicked(view: View?){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}