package com.akash.a500px

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akash.a500px.helper.DateFunctionality
import com.akash.a500px.model.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.w3c.dom.Text

class FullScreenImageViewActivity: AppCompatActivity() {

    companion object{
        val EXTRA_DATA_PHOTO = "EXTRA_DATA_PHOTO"

        fun lauch(photo: Photo, appCompatActivity: AppCompatActivity){
            val intent = Intent(appCompatActivity, FullScreenImageViewActivity::class.java)
            intent.putExtra(EXTRA_DATA_PHOTO, photo)
            appCompatActivity.startActivity(intent)
        }
    }

    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var photo: Photo
    lateinit var title: TextView
    lateinit var subTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        supportActionBar?.hide()

        imageView = findViewById(R.id.image_view)
        progressBar = findViewById(R.id.progress_bar)
        title = findViewById(R.id.title)
        subTitle = findViewById(R.id.sub_title)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            onBackPressed()
        }

        photo = intent.getParcelableExtra(EXTRA_DATA_PHOTO)

        Glide.with(this)
            .asBitmap()
            .load(photo.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

            })
            .into(imageView)


        title.text = photo.name
        val subTitleText = getString(R.string.by) + " " + photo.user?.getFullName() + " : " + DateFunctionality.getTimeString(this, photo.createdAt)
        subTitle.text = subTitleText

    }
}