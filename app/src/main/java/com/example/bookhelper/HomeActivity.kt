package com.example.bookhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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



}