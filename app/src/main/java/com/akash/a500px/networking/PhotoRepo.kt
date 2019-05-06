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

    fun getPhotosFromServer(callBack: SimpleApiCallBack<List<Photo>>){
        AndroidNetworking.get(Config.getPhotoUrl).build().getAsJSONObject(object : JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                val factoryList = Photo.factoryList(response)
                callBack.onResponse(factoryList, true)
            }

            override fun onError(anError: ANError?) {
                // handle fail response
            }

        })
    }
}