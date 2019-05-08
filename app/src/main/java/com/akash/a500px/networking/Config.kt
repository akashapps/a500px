package com.akash.a500px.networking

import com.akash.a500px.BuildConfig

class Config {
    companion object{
        val NUMBER_OF_COLUMN = 3

        fun getPhotoUrl(arg: String): String{
            return "https://api.500px.com/v1/photos?feature=popular&consumer_key=" + BuildConfig.ACCESSKEY + "&" + arg
        }

        fun getSinglePhotoUrl(id: String): String{
            return "https://api.500px.com/v1/photos/" + id + "?consumer_key=" + BuildConfig.ACCESSKEY
        }
    }
}