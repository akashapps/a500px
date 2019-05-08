package com.akash.a500px

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akash.a500px.adapter.PhotoGridAdapter
import com.akash.a500px.model.Photo
import com.akash.a500px.networking.Config
import com.akash.a500px.public_interface.ScrollListener
import com.akash.a500px.viewmodel.PhotoViewModel
import android.transition.Fade
import android.view.View


class MainActivity : AppCompatActivity(), ScrollListener {

    lateinit var recyclerView: RecyclerView
    var adapter: PhotoGridAdapter? = null
    var photoViewModel: PhotoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(PhotoViewModel::class.java)

        recyclerView = findViewById(R.id.recycler_view)
        val layoutManager = GridLayoutManager(this, Config.NUMBER_OF_COLUMN)
        recyclerView.layoutManager = layoutManager

        adapter = PhotoGridAdapter(this);
        recyclerView.adapter = adapter

        layoutManager.spanSizeLookup = adapter!!.spanLookup

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
