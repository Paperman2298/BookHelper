package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class BookDetailActivity : AppCompatActivity() {
    lateinit var title: TextView
    lateinit var author: TextView
    lateinit var pages: TextView
    lateinit var book: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        title = findViewById(R.id.detail_title)
        author = findViewById(R.id.detail_author)
        pages = findViewById(R.id.detail_pages)

        book = intent.getStringArrayListExtra("book") as ArrayList<String>

        title.text = book[0]
        author.text = book[1]
        pages.text = book[2]
    }

    fun backToHome(v: View?){
        finish();
    }


}