package com.akash.a500px.networking

import com.akash.a500px.public_interface.SimpleApiCallBack
import com.androidnetworking.AndroidNetworking

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

    fun getPhotosFromServer(callBack: SimpleApiCallBack<Any>){

//        AndroidNetworking.get(Config.getPhotoUrl)
    }
}