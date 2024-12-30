package com.adista.finalproject.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("urlImage")
    fun ImageView.loadUrlString(urlImage: String?) {
        setImageBitmap(null)
        urlImage?.let { value ->
            Glide.with(context)
                .load(value)
                .into(this)
        }
    }
}