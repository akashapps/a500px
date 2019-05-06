package com.akash.a500px.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.akash.a500px.R
import com.akash.a500px.model.Photo
import com.akash.a500px.public_interface.ScrollListener
import com.akash.a500px.view_holder.PhotoViewHolder

class PhotoGridAdapter(private val context: Context): RecyclerView.Adapter<PhotoViewHolder>() {

    var scrollListener: ScrollListener? = null
    var photoList = ArrayList<Photo>()
    val spanLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(i: Int): Int {
            return photoList[i].numOfColumn
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_photo, p0, false))
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

        p0.setData(photoList[p1])
    }

    fun addData(it: List<Photo>?) {
        if (it == null){
            return
        }

        photoList.addAll(it)

        notifyItemRangeInserted(photoList.size - 1, it.size)
    }
}