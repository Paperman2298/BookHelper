package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_home)

        val titles = ArrayList<String>()
        val authors = ArrayList<String>()
        val pages = ArrayList<String>()


        Firebase.firestore.collection("books")
            .get()
            .addOnSuccessListener {
                for(documento in it){
                    titles.add(documento.data.getValue("title").toString())
                    authors.add(documento.data.getValue("author").toString())
                    pages.add(documento.data.getValue("pages").toString())
                    //Log.d("FIRESTORE", "books: ${documento.data}")
                }

                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                val adapter = CustomAdapter(titles, authors, pages)

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter

            }
            .addOnFailureListener{
                Log.e("FIRESTORE", "error al leer books: ${it.message}")
            }
    }

    fun onCardClick(v : View){
        val intent = Intent(this, BookDetailActivity::class.java)
        val book = ArrayList<String>()
        val curTitle: TextView = v.findViewById(R.id.item_title)
        val curAuthor: TextView = v.findViewById(R.id.item_author)
        val curPages: TextView = v.findViewById(R.id.item_pages)
        book.add(curTitle.text.toString())
        book.add(curAuthor.text.toString())
        book.add(curPages.text.toString())

        intent.putExtra("book", book)
        startActivity(intent)
    }

}