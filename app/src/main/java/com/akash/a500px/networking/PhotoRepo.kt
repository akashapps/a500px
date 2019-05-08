package com.akash.a500px.networking

import com.akash.a500px.model.Photo
import com.akash.a500px.public_interface.SimpleApiCallBack
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject

/**
 * It is singleton class.
 * It is responsible for network request and cache data.
 */
class PhotoRepo private constructor(){

    companion object {
        private var instance = PhotoRepo()

        @Synchronized
        fun getInstance(): PhotoRepo{
            return instance
        }
    }

    fun getPhotosFromServer(pagingParam: PagingParam, callBack: SimpleApiCallBack<List<Photo>>){

        //TODO:: WE CAN IMPLEMENT CACHE HERE TO PREVENT SENDING SAME REQUEST TO SERVER.
        //TODO:: OR WE CAN SAVE DATA LOCALLY USING ROOM

        AndroidNetworking.get(Config.getPhotoUrl(pagingParam.toString())).build().getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                pagingParam.totalPage = response?.getInt("total_pages")!!
                val factoryList = Photo.factoryList(response)
                callBack.onResponse(factoryList, true)
            }

            override fun onError(anError: ANError?) {
                // handle fail response
            }

        })
    }
}