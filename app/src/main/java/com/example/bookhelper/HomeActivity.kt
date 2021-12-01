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
import com.google.firebase.storage.ktx.storage
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_home)


        val titles = ArrayList<String>()
        val authors = ArrayList<String>()
        val pages = ArrayList<String>()
        val uids = ArrayList<String>()

        if(user != null){
            db.collection("users").whereEqualTo("email", user.email).get().addOnSuccessListener {
                var myBooks : ArrayList<String> = ArrayList<String>()

                for(doc in it){
                    myBooks = doc.data.getValue("books") as ArrayList<String>
                }

                for(book in myBooks){
                    db.collection("books").document(book).get().addOnSuccessListener {
                        titles.add(it.data?.getValue("title").toString())
                        authors.add(it.data?.getValue("author").toString())
                        pages.add(it.data?.getValue("pages").toString())
                        uids.add(it.data?.getValue("uid").toString())

                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        val adapter = CustomAdapter(titles, authors, pages, uids, this)

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.adapter = adapter
                    }.addOnFailureListener{
                        Log.e("FIRESTORE", "error al leer books: ${it.message}")
                    }


                }





            }


        }

    }

    fun onCardClick(v : View){
        val intent = Intent(this, BookDetailActivity::class.java)
        val book = ArrayList<String>()
        val curTitle: TextView = v.findViewById(R.id.item_book_title)
        val curAuthor: TextView = v.findViewById(R.id.item_author)
        val curPages: TextView = v.findViewById(R.id.item_pages)
        book.add(curTitle.text.toString())
        book.add(curAuthor.text.toString())
        book.add(curPages.text.toString())

        intent.putExtra("book", curTitle.text.toString())
        startActivity(intent)
        finish()
    }

    fun goToAddBookActivity(v : View){
        val intent = Intent(this, AddBookActivity::class.java)
        startActivity(intent)
    }

    fun goToProfileActivity(v : View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

}