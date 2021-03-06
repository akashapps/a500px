package com.akash.a500px.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akash.a500px.FullScreenImageViewActivity
import com.akash.a500px.R
import com.akash.a500px.model.Photo
import com.akash.a500px.public_interface.ScrollListener
import com.akash.a500px.view_holder.PhotoViewHolder

class PhotoGridAdapter(private val context: AppCompatActivity): RecyclerView.Adapter<PhotoViewHolder>() {

    var scrollListener: ScrollListener? = null
    var photoList = ArrayList<Photo>()
    val spanLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(i: Int): Int {
            return photoList[i].column
        }

        override fun isSpanIndexCacheEnabled(): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_photo, p0, false))
    }

    override fun getItemViewType(position: Int): Int {
        return photoList[position].column
    }

    override fun getItemCount(): Int {
        Log.e(PhotoGridAdapter::class.java.simpleName, "getItemCount: " + photoList.size)
        return photoList.size
    }

    override fun onBindViewHolder(p0: PhotoViewHolder, p1: Int) {
        Log.e(PhotoGridAdapter::class.java.simpleName, "position: " + p1.toString())
        if (p1 == photoList.size - 1){
            scrollListener?.recyclerViewDidScrollBottom()
        }

        val photo = photoList[p1]
        p0.setData(photo)

        p0.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                v?.let { ViewCompat.setTransitionName(it, "example_transition") }
                FullScreenImageViewActivity.lauch(v, photo, context)
            }
        })
    }

    fun addData(it: List<Photo>?) {
        if (it == null){
            return
        }

        photoList.clear()
        photoList.addAll(it)

        notifyDataSetChanged()

//        photoList.addAll(it)
//
//        notifyItemRangeInserted(photoList.size - 1, it.size)
    }
}