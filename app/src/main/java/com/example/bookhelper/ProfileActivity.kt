package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewParent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var uid : TextView
    lateinit var lastBook : TextView
    lateinit var page : TextView
    private lateinit var list : ListView
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var arrayAdapter : ArrayAdapter<String>

        name = findViewById(R.id.profileNameTextView)
        email = findViewById(R.id.profileEmailTextView)
        uid = findViewById(R.id.profileIdTextView)
        lastBook = findViewById(R.id.profileLastBookTextView)
        page = findViewById(R.id.profilePageTextView)
        list = findViewById(R.id.profileListView)

        if(user != null){

            uid.text = user.uid
            email.text = user.email

            val docRef = db.collection("users").whereEqualTo("email", user.email)
            docRef.get().addOnSuccessListener {
                for(document in it){
                    name.text = "¡Welcome ${document.data.getValue("name")}!"
                    val books = document.data.getValue("books") as ArrayList<String>
                    arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, books)

                    lastBook.text = books[books.size - 1]
                    list.adapter = arrayAdapter

                    list.setOnItemClickListener(){parent, v, position, id ->
                        Toast.makeText(this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun goToAddBookActivity(v : View){
        val intent = Intent(this, AddBookActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToHomeActivity(v : View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}