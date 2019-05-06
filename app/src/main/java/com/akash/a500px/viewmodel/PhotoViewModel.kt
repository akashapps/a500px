package com.akash.a500px.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.akash.a500px.model.Photo
import com.akash.a500px.networking.PagingParam
import com.akash.a500px.networking.PhotoRepo
import com.akash.a500px.public_interface.SimpleApiCallBack

class PhotoViewModel(context: Application) : AndroidViewModel(context) {

    var photoList: MutableLiveData<List<Photo>> = MutableLiveData()
    var pagingParam = PagingParam()

    init {
        loadData()
    }

    private fun loadData(){
        PhotoRepo.getInstance().getPhotosFromServer(pagingParam, object : SimpleApiCallBack<List<Photo>> {
            override fun onResponse(data: List<Photo>, success: Boolean) {
                addData(data)
            }
        })
    }

    fun addData(list: List<Photo>) {
        photoList.value = list
    }

    fun loadNext(){
        pagingParam.next()
        loadData()
    }
}