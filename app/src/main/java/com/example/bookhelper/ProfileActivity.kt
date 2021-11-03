package com.example.bookhelper

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap.CompressFormat

import android.graphics.BitmapFactory
import com.bumptech.glide.Glide


class ProfileActivity : AppCompatActivity() {

    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var username : EditText
    lateinit var lastBook : TextView
    lateinit var page : TextView
    lateinit var picture : ImageView
    lateinit var btn : FloatingActionButton

    private lateinit var list : ListView
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private val storageRef = Firebase.storage.reference
    private var uid = ""

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        val image = result.data?.extras?.get("data") as Bitmap
        picture.setImageBitmap(image)

        val profilePictureRef = storageRef.child("images/profile/${user?.uid}")
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
        setContentView(R.layout.activity_profile)
        var arrayAdapter : ArrayAdapter<String>

        name = findViewById(R.id.profileNameTextView)
        email = findViewById(R.id.profileEmailTextView)
        username = findViewById(R.id.usernameET)
        lastBook = findViewById(R.id.profileLastBookTextView)
        page = findViewById(R.id.profilePageTextView)
        list = findViewById(R.id.profileListView)
        picture = findViewById(R.id.profilePicture)
        btn = findViewById(R.id.ppBtn)

        if(user != null){
            email.text = user.email

            storageRef.child("images/profile/${user.uid}").downloadUrl.addOnSuccessListener { result ->
                Glide.with(this).load(result.toString()).into(picture)
            }

            val docRef = db.collection("users").whereEqualTo("email", user.email)
            docRef.get().addOnSuccessListener {
                for(document in it){
                    name.text = "¡Welcome ${document.data.getValue("name")}!"
                    username.setText(document.data.getValue("name").toString())
                    uid = document.data.getValue("uid").toString()

                    val books = document.data.getValue("books") as ArrayList<String>
                    val currentPages = ArrayList<String>()
                    currentPages.add("31")
                    currentPages.add("22")

                    if(books.size > 0){
                        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, books)
                        lastBook.text = books[books.size - 1]
                        list.adapter = arrayAdapter
                    }

                    list.setOnItemClickListener(){parent, v, position, id ->
                        Toast.makeText(this, "Current Page: ${currentPages[position]}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun takePicture(v : View){
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePhotoIntent)
    }

    fun goToAddBookActivity(v : View){
        val intent = Intent(this, AddBookActivity::class.java)
        startActivity(intent)
    }

    fun goToHomeActivity(v : View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun logOut(v : View){
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun updateInfo(v : View){
        val docRef = Firebase.firestore.collection("users").document(uid)
        Log.e("Aaaaaaa", uid)
        docRef.update("name", username.text.toString()).addOnSuccessListener {
            Toast.makeText(this, "¡Succesful update!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish();
        }
    }
}