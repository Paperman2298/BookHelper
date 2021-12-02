package com.example.bookhelper

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
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

    lateinit var spinner : Spinner
    var myBooks : ArrayList<String> = ArrayList<String>()
    var titles = ArrayList<String>()
    var authors = ArrayList<String>()
    var pages = ArrayList<String>()
    var uids = ArrayList<String>()
    var currentPages = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_home)


        if(user != null){
            db.collection("users").whereEqualTo("email", user.email).get().addOnSuccessListener {

                for(doc in it){
                    myBooks = doc.data.getValue("books") as ArrayList<String>
                }

                for(book in myBooks){
                    db.collection("books").document(book).get().addOnSuccessListener {
                        titles.add(it.data?.getValue("title").toString())
                        authors.add(it.data?.getValue("author").toString())
                        pages.add(it.data?.getValue("pages").toString())
                        uids.add(it.data?.getValue("uid").toString())
                        currentPages.add(it.data?.getValue("current_page").toString())

                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        val adapter = CustomAdapter(titles, authors, pages, uids, currentPages, this)

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.adapter = adapter

                    }.addOnFailureListener{
                        Log.e("FIRESTORE", "error al leer books: ${it.message}")
                    }
                }
            }
        }

        spinner = findViewById(R.id.homeSpinner)

        db.collection("tools").document("genres").get().addOnSuccessListener {
            val myGenres = it.data?.getValue("list") as ArrayList<String>

            if(spinner != null){
                val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, myGenres)
                spinner.adapter = adapter
            }
        }

    }

    fun filter(v : View){
        val myGenre = spinner.selectedItem.toString()
        titles = ArrayList<String>()
        authors = ArrayList<String>()
        pages = ArrayList<String>()
        uids = ArrayList<String>()
        currentPages = ArrayList<String>()

        for(book in myBooks){
            db.collection("books").document(book).get().addOnSuccessListener {
                if(it.data?.getValue("genre") == myGenre){
                    titles.add(it.data?.getValue("title").toString())
                    authors.add(it.data?.getValue("author").toString())
                    pages.add(it.data?.getValue("pages").toString())
                    uids.add(it.data?.getValue("uid").toString())
                    currentPages.add(it.data?.getValue("current_page").toString())

                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                    val adapter = CustomAdapter(titles, authors, pages, uids, currentPages, this)

                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = adapter
                }

            }
        }
    }

    fun onCardClick(v : View){
        Log.e("VIEW", "${v}")
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

    private fun checkColor(v : View){
        val curr : TextView = v.findViewById(R.id.item_current)
        val pages : TextView = v.findViewById(R.id.item_pages)

        if(curr.text.toString() == pages.text.toString()){
            v.setBackgroundColor(Color.GREEN)
        }

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