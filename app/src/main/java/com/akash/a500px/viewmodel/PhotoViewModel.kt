package com.akash.a500px.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.akash.a500px.model.Photo
import com.akash.a500px.networking.Config
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

        var value = photoList.value

        if (value == null){
            value = list
        }else{
            (value as ArrayList).addAll(list)
        }

        photoList.value = calculateColumn(value)
    }

    fun loadNext(){
        pagingParam.next()
        loadData()
    }

    fun calculateColumn(photoList: List<Photo>): List<Photo>{
        val result = ArrayList<Photo>()
        val row = ArrayList<Photo>()
        var rowRatio = 0f

        for (photo in photoList) {

            photo.resetColumnAndImageRatio()

            var ratio = photo.width / photo.height.toFloat()

            photo.imageRatio = ratio

            result.add(photo)
            rowRatio += ratio

            if (rowRatio > 2f) {
                var used = 0

                for (photoRow in row) {
                    photoRow.column = ((Config.NUMBER_OF_COLUMN * photoRow.column) / rowRatio).toInt()
                    used += photoRow.column
                }

                photo.column = Config.NUMBER_OF_COLUMN - used

                row.clear()
                rowRatio = 0f

            } else {
                row.add(photo)
            }
        }
        return result
    }
}