package com.akash.a500px

import android.app.Application
import com.androidnetworking.AndroidNetworking

class PxApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        AndroidNetworking.initialize(this)
    }
}
