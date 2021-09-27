package com.example.bookhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    lateinit var name : TextView
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser

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
}