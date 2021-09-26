package com.example.bookhelper

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class CustomAdapter(a : ArrayList<String>, b : ArrayList<String>, c : ArrayList<String> ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    //val titles = arrayOf("A Brave New World", "Why I Write", "Man's Search For Meaning")
    //val authors = arrayOf("Aldous Huxley", "George Orwell", "Viktor E. Frankl")
    //val pages = arrayOf("197", "120", "165")
    val titles = a
    val authors = b
    val pages = c

    val images = intArrayOf(titles.size)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemAuthor.text = authors[i]
        viewHolder.itemPages.text = pages[i]
        viewHolder.itemImage.setImageResource(R.drawable.ic_launcher_background)
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
            itemTitle = itemView.findViewById(R.id.item_title)
            itemAuthor = itemView.findViewById(R.id.item_author)
            itemPages = itemView.findViewById(R.id.item_pages)
        }
    }



}