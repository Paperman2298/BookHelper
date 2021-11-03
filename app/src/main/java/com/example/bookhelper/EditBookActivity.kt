package com.example.bookhelper

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class EditBookActivity : AppCompatActivity() {
    private var uid = ""
    private val db = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    lateinit var title: EditText
    lateinit var review: EditText
    lateinit var author: EditText
    lateinit var pages: EditText
    lateinit var curr: EditText
    lateinit var genre: EditText
    lateinit var book: ImageView

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val image = result.data?.extras?.get("data") as Bitmap
        book.setImageBitmap(image)

        val profilePictureRef = storageRef.child("images/books/${uid}")
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = profilePictureRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.e("FOTO", "NO SE SUBIO LA FOTO")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.e("FOTO", "SE SUBIO LA FOTO")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)
        uid = intent.getStringExtra("book").toString()

        title = findViewById(R.id.editTitle)
        author = findViewById(R.id.editAuthor)
        pages = findViewById(R.id.editPages)
        curr = findViewById(R.id.editCurrentPage)
        genre = findViewById(R.id.editGenre)
        review = findViewById(R.id.editReview)
        book = findViewById(R.id.editImage)

        val docRef = db.collection("books").whereEqualTo("uid", uid)
        docRef.get().addOnSuccessListener {
            for(doc in it){
                title.setText(doc.data.getValue("title").toString())
                author.setText(doc.data.getValue("author").toString())
                pages.setText(doc.data.getValue("pages").toString())
                curr.setText(doc.data.getValue("current_page").toString())
                genre.setText(doc.data.getValue("genre").toString())
                review.setText(doc.data.getValue("review").toString())
            }

            storageRef.child("images/books/${uid}").downloadUrl.addOnSuccessListener { result ->
                Glide.with(this).load(result.toString()).into(book)
            }

        }
    }

    fun backToHome(v: View?){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish();
    }

    fun takePicture(v : View){
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePhotoIntent)
    }

    fun update(v: View?){
        val docRef = Firebase.firestore.collection("books").document(uid)

        docRef.update("title", title.text.toString()).addOnSuccessListener {
            docRef.update("author", author.text.toString()).addOnSuccessListener {
                docRef.update("pages", pages.text.toString()).addOnSuccessListener {
                    docRef.update("current_page", curr.text.toString()).addOnSuccessListener {
                        docRef.update("genre", genre.text.toString()).addOnSuccessListener {
                            docRef.update("review", review.text.toString()).addOnSuccessListener {
                                Toast.makeText(this, "¡Succesful update!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish();
                            }
                        }
                    }
                }
            }
        }
    }

}