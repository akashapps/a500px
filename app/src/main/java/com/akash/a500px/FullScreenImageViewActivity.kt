package com.akash.a500px

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.akash.a500px.helper.DateFunctionality
import com.akash.a500px.model.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import android.transition.Fade
import com.akash.a500px.networking.PhotoRepo
import com.akash.a500px.public_interface.SimpleApiCallBack
import org.json.JSONObject

class FullScreenImageViewActivity: AppCompatActivity() {

    companion object{
        val EXTRA_DATA_PHOTO = "EXTRA_DATA_PHOTO"

        fun lauch(v: View?, photo: Photo, appCompatActivity: AppCompatActivity){
            val intent = Intent(appCompatActivity, FullScreenImageViewActivity::class.java)
            var option = v?.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(appCompatActivity, it,
                    ViewCompat.getTransitionName(v).toString()
                )
            }
            intent.putExtra(EXTRA_DATA_PHOTO, photo)
            appCompatActivity.startActivity(intent, option?.toBundle())
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

        val fade = Fade()
        val decor = window.decorView
        fade.excludeTarget(decor.findViewById<View>(R.id.action_bar_container), true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = fade
        window.exitTransition = fade

        supportActionBar?.hide()

        imageView = findViewById(R.id.image_view)
        progressBar = findViewById(R.id.progress_bar)
        title = findViewById(R.id.title)
        subTitle = findViewById(R.id.sub_title)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            onBackPressed()
        }

        photo = intent.getParcelableExtra(EXTRA_DATA_PHOTO)

        loadImageToImageView(photo.imageUrl)

        title.text = photo.name
        val subTitleText = getString(R.string.by) + " " + photo.user?.getFullName() + " : " + DateFunctionality.getTimeString(this, photo.createdAt)
        subTitle.text = subTitleText

        PhotoRepo.getInstance().getSinglePhoto(photo.id.toString(), object: SimpleApiCallBack<JSONObject?>{
            override fun onResponse(data: JSONObject?, success: Boolean) {
                if (success){
                    val url = data?.getJSONObject("photo")?.getString("image_url")
                    url?.let { loadImageToImageView(it) }
                }
            }
        })
    }

    fun loadImageToImageView(url: String){
        Glide.with(this)
            .asBitmap()
            .load(url)
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
                    supportPostponeEnterTransition()
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
                    supportPostponeEnterTransition()
                    return false
                }

            })
            .into(imageView)
    }
}