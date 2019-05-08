package com.akash.a500px.view_holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.akash.a500px.R
import com.akash.a500px.model.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    lateinit var imageView: ImageView

    init {
        imageView = itemView.findViewById(R.id.image_view)
    }

    fun setData(photo: Photo) {

        Glide.with(itemView.context)
            .load(photo.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(imageView)

    }
}