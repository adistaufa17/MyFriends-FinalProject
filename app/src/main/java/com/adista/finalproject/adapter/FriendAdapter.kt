package com.adista.finalproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adista.finalproject.R
import com.adista.finalproject.data.DataProduct

class FriendAdapter(
    private val context: Context,
    private var items: List<DataProduct>,
    private val listener: OnFriendClickListener
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private var originalItems: List<DataProduct> = items

    interface OnFriendClickListener {
        fun onFriendClick(itemId: Int)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val product = items[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            listener.onFriendClick(product.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(itemView)
    }

    override fun getItemCount(): Int = items.size

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private val imageView: ImageView = itemView.findViewById(R.id.iv_photo)
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_name)
        private val schoolTextView: TextView = itemView.findViewById(R.id.tv_school)

        fun bind(product: DataProduct) {
            //imageView.setImageResource(R.drawable.profile) // Or use a placeholder image

            nameTextView.text = product.title
            schoolTextView.text = product.description
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newProducts: List<DataProduct>) {
        this.items = newProducts
        this.originalItems = newProducts
        notifyDataSetChanged()
    }
}