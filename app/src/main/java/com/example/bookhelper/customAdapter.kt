package com.example.bookhelper

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.ArrayList

class CustomAdapter(a : ArrayList<String>, b : ArrayList<String>, c : ArrayList<String>, d : ArrayList<String>, act: Activity) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    //val titles = arrayOf("A Brave New World", "Why I Write", "Man's Search For Meaning")
    //val authors = arrayOf("Aldous Huxley", "George Orwell", "Viktor E. Frankl")
    //val pages = arrayOf("197", "120", "165")
    val titles = a
    val authors = b
    val pages = c
    val uids = d
    val activity = act
    private val storageRef = Firebase.storage.reference

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemAuthor.text = authors[i]
        viewHolder.itemPages.text = pages[i]
        // viewHolder.itemImage.setImageResource(R.drawable.ic_launcher_background)

        storageRef.child("images/books/${uids[i]}").downloadUrl.addOnSuccessListener { result ->
            Glide.with(activity).load(result.toString()).into(viewHolder.itemImage)
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemAuthor : TextView
        var itemPages : TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_book_title)
            itemAuthor = itemView.findViewById(R.id.item_author)
            itemPages = itemView.findViewById(R.id.item_pages)
        }
    }



}