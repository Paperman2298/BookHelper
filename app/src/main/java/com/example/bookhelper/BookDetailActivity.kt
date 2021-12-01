package com.example.bookhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BookDetailActivity : AppCompatActivity() {
    lateinit var title: TextView
    lateinit var review: TextView
    lateinit var author: TextView
    lateinit var pages: TextView
    lateinit var curr: TextView
    lateinit var genre: TextView
    lateinit var book: ImageView

    private val db = Firebase.firestore
    private val storageRef = Firebase.storage.reference
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        title = findViewById(R.id.bookDetailTitleTextView)
        author = findViewById(R.id.authorTV)
        pages = findViewById(R.id.pagesTV)
        curr = findViewById(R.id.currentPageTV)
        genre = findViewById(R.id.genreTV)
        review = findViewById(R.id.reviewTV)
        book = findViewById(R.id.bookIV)

        val docRef = db.collection("books").whereEqualTo("title", intent.getStringExtra("book"))
        docRef.get().addOnSuccessListener {
            for(doc in it){
                title.setText("Title: " + doc.data.getValue("title").toString())
                author.setText("Author: " + doc.data.getValue("author").toString())
                pages.setText("Pages: " + doc.data.getValue("pages").toString())
                curr.setText("Current Page: " + doc.data.getValue("current_page").toString())
                genre.setText("Genre: " + doc.data.getValue("genre").toString())
                review.setText("Review: " + doc.data.getValue("review").toString())
                uid = doc.data.getValue("uid").toString()
            }

            storageRef.child("images/books/${uid}").downloadUrl.addOnSuccessListener { result ->
                Glide.with(this).load(result.toString()).into(book)
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

    fun publishBook(v : View?){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "I'm currently reading this book: ${title.text.slice(7 until title.length())}!!!")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun backToHome(v: View?){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish();
    }

    fun delete(v: View?){
        db.collection("books").document(uid).delete().addOnSuccessListener{
            Toast.makeText(this, "¡Libro eliminado!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun goToEditActivity(v: View?){
        val intent = Intent(this, EditBookActivity::class.java)
        intent.putExtra("book", uid)
        startActivity(intent)
        finish();
    }

}