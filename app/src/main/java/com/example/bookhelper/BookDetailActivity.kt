package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BookDetailActivity : AppCompatActivity() {
    lateinit var title: TextView
    lateinit var review : EditText
    lateinit var author: TextView
    lateinit var pages: TextView
    lateinit var book: ArrayList<String>
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        title = findViewById(R.id.bookDetailTitleTextView)
        review = findViewById(R.id.bookDetailMT)

        title.text = intent.getStringExtra("book")
        val docRef = db.collection("books").whereEqualTo("title", intent.getStringExtra("book"))
        docRef.get().addOnSuccessListener {
            for(doc in it){
                review.setText(doc.data.getValue("review").toString())
            }
        }

//        title = findViewById(R.id.detail_title)
//        author = findViewById(R.id.detail_author)
//        pages = findViewById(R.id.detail_pages)

//        book = intent.getStringArrayListExtra("book") as ArrayList<String>

//        title.text = book[0]
//        author.text = book[1]
//        pages.text = book[2]
    }

    fun backToHome(v: View?){
        finish();
    }


}