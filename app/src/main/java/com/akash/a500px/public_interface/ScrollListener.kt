package com.akash.a500px.public_interface

import android.view.MotionEvent

interface ScrollListener {
    fun recyclerViewDidScrollTop()
    fun recyclerViewDidScrollBottom()
    fun recyclerViewStopScrolling()
    fun recyclerViewStartScrolling(direction: Int)
    fun recyclerViewOnTouchEvent(event: MotionEvent)
}