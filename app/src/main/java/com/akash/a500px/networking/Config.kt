package com.akash.a500px.networking

import com.akash.a500px.BuildConfig
import java.util.*

class Config {

    companion object{
        val getPhotoUrl = "https://api.500px.com/v1/photos?feature=popular&&consumer_key=" + BuildConfig.ACCESSKEY
    }
}