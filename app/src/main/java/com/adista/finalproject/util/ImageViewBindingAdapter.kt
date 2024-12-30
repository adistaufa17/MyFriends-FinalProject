package com.adista.finalproject.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.adista.finalproject.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object ImageViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("urlImage")
    fun ImageView.loadUrlString(urlImage: String?) {
        setImageBitmap(null)
        urlImage?.let { value ->
            Glide.with(context)
                .load(value)
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.loading_animation) // Add a loading animation drawable
                    .error(R.drawable.image_placeholder) // Add an error placeholder drawable
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop())
                .into(this)
        }
    }
}