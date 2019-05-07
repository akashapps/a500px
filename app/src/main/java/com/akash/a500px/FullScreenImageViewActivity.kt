package com.akash.a500px

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.akash.a500px.model.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ortiz.touchview.TouchImageView

class FullScreenImageViewActivity: AppCompatActivity() {

    companion object{
        val EXTRA_DATA_PHOTO = "EXTRA_DATA_PHOTO"

        fun lauch(photo: Photo, appCompatActivity: AppCompatActivity){
            val intent = Intent(appCompatActivity, FullScreenImageViewActivity::class.java)
            intent.putExtra(EXTRA_DATA_PHOTO, photo)
            appCompatActivity.startActivity(intent)
        }
    }

    lateinit var imageView: TouchImageView
    lateinit var progressBar: ProgressBar
    lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        imageView = findViewById(R.id.image_view)
        progressBar = findViewById(R.id.progress_bar)

        photo = intent.getParcelableExtra(EXTRA_DATA_PHOTO)

        Glide.with(this)
            .load(photo.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.VISIBLE
                    return false
                }

            })
            .into(imageView)

    }
}