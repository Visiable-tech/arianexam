package com.arianinstitute.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nctapplication.commons.MyApp


@BindingAdapter("imageUrl", "error")
fun loadImage(view: AppCompatImageView, url: String, error: Drawable) {
    if (url == null) {
        view.setImageDrawable(error);
    } else {
        MyApp.getInstance()
            ?.let { Glide.with(it.applicationContext).load(url).into(view)}
    }

}