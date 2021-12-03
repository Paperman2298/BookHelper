package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class FriendsBooksActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    lateinit var friend : String
    lateinit var books : ArrayList<String>
    lateinit var name : TextView

    var titles = ArrayList<String>()
    var authors = ArrayList<String>()
    var pages = ArrayList<String>()
    var uids = ArrayList<String>()
    var currentPages = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_books)
        name = findViewById(R.id.friendsBooksTextView)
        friend = intent.getStringExtra("friend").toString()

        if(user != null && friend != null){
            db.collection("users").document(friend).get().addOnSuccessListener {
                books = it.data?.getValue("books") as ArrayList<String>
                name.text = it.data?.getValue("name") as String + "'s\nbooks"

                for(book in books){
                    db.collection("books").document(book).get().addOnSuccessListener {
                        titles.add(it.data?.getValue("title").toString())
                        authors.add(it.data?.getValue("author").toString())
                        pages.add(it.data?.getValue("pages").toString())
                        uids.add(it.data?.getValue("uid").toString())
                        currentPages.add(it.data?.getValue("current_page").toString())

                        val recyclerView = findViewById<RecyclerView>(R.id.friendsBooksRecyclerView)
                        val adapter = CustomAdapter(titles, authors, pages, uids, currentPages, this)

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.adapter = adapter

                    }.addOnFailureListener{
                        Log.e("FIRESTORE", "error al leer books: ${it.message}")
                    }
                }
            }
        }
    }

    fun goBack(v : View){
        finish()
    }
}