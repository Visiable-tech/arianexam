package com.onlinetalentsearchexam.adapter

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.avision.commons.OnlineTalentSearchExam


@BindingAdapter("imageUrl", "error")
fun loadImage(view: AppCompatImageView, url: String, error: Drawable) {
    if (url == null) {
        view.setImageDrawable(error);
    } else {
        OnlineTalentSearchExam.getInstance()
            ?.let { Glide.with(it.applicationContext).load(url).into(view)}
    }

}