package com.akash.a500px

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import com.akash.a500px.adapter.PhotoGridAdapter
import com.akash.a500px.model.Photo
import com.akash.a500px.networking.Config
import com.akash.a500px.public_interface.ScrollListener
import com.akash.a500px.viewmodel.PhotoViewModel

class MainActivity : AppCompatActivity(), ScrollListener {

    lateinit var recyclerView: RecyclerView
    var adapter: PhotoGridAdapter? = null
    var photoViewModel: PhotoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(PhotoViewModel::class.java)

        recyclerView = findViewById(R.id.recycler_view)
        adapter = PhotoGridAdapter(this);

        val layoutManager = GridLayoutManager(this, Config.NUMBER_OF_COLUMN)
        layoutManager.spanSizeLookup = adapter!!.spanLookup

        recyclerView.adapter = adapter

        recyclerView.layoutManager = layoutManager

        adapter!!.scrollListener = this

        observeViewModel()
    }

    private fun observeViewModel() {
        photoViewModel?.photoList?.observe(this, Observer<List<Photo>> {
            adapter?.addData(it)
        })
    }

    override fun recyclerViewDidScrollBottom() {
        photoViewModel?.loadNext()
    }

    override fun recyclerViewDidScrollTop() {

    }

    override fun recyclerViewStopScrolling() {

    }

    override fun recyclerViewStartScrolling(direction: Int) {

    }

    override fun recyclerViewOnTouchEvent(event: MotionEvent) {

    }
}
