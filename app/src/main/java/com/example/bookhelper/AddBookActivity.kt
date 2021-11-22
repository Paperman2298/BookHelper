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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AddBookActivity : AppCompatActivity() {

    lateinit var title : EditText
    lateinit var author : EditText
    lateinit var pages : EditText
    lateinit var currentPage : EditText
    lateinit var genre : EditText
    lateinit var review : EditText
    lateinit var btn : FloatingActionButton
    lateinit var picture : ImageView
    lateinit var bitmap : Bitmap

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private val storageRef = Firebase.storage.reference

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        bitmap = result.data?.extras?.get("data") as Bitmap
        picture.setImageBitmap(bitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        title = findViewById(R.id.addBookTitleET)
        author = findViewById(R.id.addBookAuthorET)
        pages = findViewById(R.id.addBookPagesET)
        currentPage = findViewById(R.id.addBookCurrentPageET)
        genre = findViewById(R.id.addBookGenreET)
        review = findViewById(R.id.addBookReviewTM)
        btn = findViewById(R.id.takePhotoBtn)
        picture = findViewById(R.id.bookPreviewImageView)

    }

    fun addBook(v : View){

        if(author.text.isEmpty()){
            Toast.makeText(this, "¡Campo author vacío!", Toast.LENGTH_SHORT).show()
        }else if(title.text.isEmpty()){
            Toast.makeText(this, "¡Campo título vacío!", Toast.LENGTH_SHORT).show()
        }else if(pages.text.isEmpty()){
            Toast.makeText(this, "¡Campo pages vacío!", Toast.LENGTH_SHORT).show()
        }else if(currentPage.text.isEmpty()){
            Toast.makeText(this, "¡Campo current page vacío!", Toast.LENGTH_SHORT).show()
        }else if(genre.text.isEmpty()){
            Toast.makeText(this, "¡Campo genre vacío!", Toast.LENGTH_SHORT).show()
        }else if(review.text.isEmpty()){
            Toast.makeText(this, "¡Campo review vacío!", Toast.LENGTH_SHORT).show()
        }else{
            val data = hashMapOf(
                "author" to author.text.toString(),
                "title" to title.text.toString(),
                "pages" to pages.text.toString(),
                "current_page" to currentPage.text.toString(),
                "genre" to genre.text.toString(),
                "review" to review.text.toString()
            )


            db.collection("books")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    var uid = ""
                    val docRef = db.collection("users").whereEqualTo("email", user?.email)
                    docRef.get().addOnSuccessListener {
                        for(doc in it){
                            uid = doc.data.getValue("uid").toString()
                        }

                        val docRef = Firebase.firestore.collection("books").document(documentReference.id)
                        docRef.update("uid", documentReference.id).addOnSuccessListener {

                            val userRef = db.collection("users").document(uid)
                            userRef.update("books", FieldValue.arrayUnion(title.text.toString())).addOnSuccessListener {

                                val profilePictureRef = storageRef.child("images/books/${documentReference.id}")
                                val baos = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                val data = baos.toByteArray()

                                var uploadTask = profilePictureRef.putBytes(data)
                                uploadTask.addOnSuccessListener { taskSnapshot ->
                                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                                    // ...
                                    Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "¡Error al registrar!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun takePicture(v : View){
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePhotoIntent)
    }

    fun goBack(v : View){

        finish();
    }
}