package com.akash.a500px

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.akash.a500px.model.Photo
import com.akash.a500px.viewmodel.PhotoViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val instance = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(PhotoViewModel::class.java)

        observeViewModel(instance)
    }

    private fun observeViewModel(instance: PhotoViewModel) {
        instance.photoList.observe(this, Observer<List<Photo>> {

        })
    }
}
