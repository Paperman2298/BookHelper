package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendsActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    lateinit var recyclerview: RecyclerView
    lateinit var friends: ArrayList<String>
    lateinit var data : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        recyclerview = findViewById(R.id.friendsRecyclerView)

        friends = ArrayList()
        data = ArrayList()
        if(user != null){
            db.collection("users").whereNotEqualTo("email", user.email).get().addOnSuccessListener{
                for(doc in it){
                    friends.add(doc.data.getValue("name").toString())
                    data.add(doc.data.getValue("uid").toString())
                }

                val adapter = FriendsAdapter(friends)

                // declare the layout manager
                var llm = LinearLayoutManager(this)
                llm.orientation = LinearLayoutManager.VERTICAL

                // setup the recycler view
                recyclerview.layoutManager = llm
                recyclerview.adapter = adapter
            }
        }

    }

    fun toFriendsProfile(v : View){

        val currentFriend : TextView = v.findViewById(R.id.friendsName)
        val idx = friends.indexOf(currentFriend.text.toString())
        Log.e("Friend: ", data[idx])
    }

    fun goToProfileActivity(v : View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}