package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    lateinit var name : TextView
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name = findViewById(R.id.profileNameTextView)
        if(user != null){
            val docRef = db.collection("users").whereEqualTo("email", user.email)
            docRef.get().addOnSuccessListener {
                for(document in it){
                    name.text = "¡Welcome ${document.data.getValue("name")}!"
                }
            }
        }

    }

    fun goToAddBookActivity(v : View){
        val intent = Intent(this, AddBookActivity::class.java)
        startActivity(intent)
    }
}