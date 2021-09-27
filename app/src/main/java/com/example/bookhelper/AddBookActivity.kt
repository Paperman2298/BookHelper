package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddBookActivity : AppCompatActivity() {

    lateinit var title : EditText
    lateinit var author : EditText
    lateinit var pages : EditText
    lateinit var currentPage : EditText
    lateinit var genre : EditText
    lateinit var review : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        title = findViewById(R.id.addBookTitleET)
        author = findViewById(R.id.addBookAuthorET)
        pages = findViewById(R.id.addBookPagesET)
        currentPage = findViewById(R.id.addBookCurrentPageET)
        genre = findViewById(R.id.addBookGenreET)
        review = findViewById(R.id.addBookReviewTM)
    }

    fun addBook(v : View){
        val data = hashMapOf(
            "author" to author.text.toString(),
            "title" to title.text.toString(),
            "pages" to pages.text.toString(),
            "current_page" to currentPage.text.toString(),
            "genre" to genre.text.toString(),
            "review" to review.text.toString()
        )


        Firebase.firestore.collection("books")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "¡Error al registrar!", Toast.LENGTH_SHORT).show()
            }
    }

    fun goBack(v : View){
        finish();
    }
}