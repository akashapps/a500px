package com.akash.a500px.public_interface

interface SimpleApiCallBack<T> {
    fun onResponse(data: T, success: Boolean)
}