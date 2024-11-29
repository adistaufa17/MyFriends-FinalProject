package com.adista.finalproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adista.finalproject.R
import com.adista.finalproject.database.Friend

class FriendAdapter(
    private val context: Context,
    private var friends: List<Friend>,
    private val listener: OnFriendClickListener
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private var originalFriends: List<Friend> = friends

    interface OnFriendClickListener {
        fun onFriendClick(friendId: Int)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)

        holder.itemView.setOnClickListener {
            listener.onFriendClick(friend.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(itemView)
    }

    override fun getItemCount(): Int = friends.size

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_photo)
        private val nameTextView = itemView.findViewById<TextView>(R.id.tv_name)
        private val schoolTextView = itemView.findViewById<TextView>(R.id.tv_school)

        fun bind(friend: Friend) {
            val photoPath = friend.photo

            if (photoPath.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeFile(photoPath)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageResource(R.drawable.profile)
                }
            } else {
                imageView.setImageResource(R.drawable.profile)
            }

            nameTextView.text = friend.name
            schoolTextView.text = friend.school
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newFriends: List<Friend>) {
        this.friends = newFriends
        this.originalFriends = newFriends
        notifyDataSetChanged()
    }
}
