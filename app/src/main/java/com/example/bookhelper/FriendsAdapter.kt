package com.example.bookhelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(private var friends : ArrayList<String>) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textView : TextView
        var button : Button

        // main constructor has no body
        // but there is an init block that is always called

        init {
            textView = itemView.findViewById(R.id.friendsName)
            button = itemView.findViewById(R.id.friendsBtn)
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsAdapter.FriendsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friends_row, parent, false)

        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsAdapter.FriendsViewHolder, position: Int) {
        holder.textView.text = friends[position]
    }

    override fun getItemCount(): Int {
        return friends.size
    }

}