package com.akash.a500px.networking

class PagingParam() {
    var currentPage: Int = 1
    var totalPage: Int = -1

    override fun toString(): String{
        return QueryBuilder().add("page", currentPage).build()
    }

    fun next(){
        currentPage += 1
    }

    fun reset(){
        currentPage = 1
        totalPage = -1
    }

    fun haveMorePage():Boolean{
        return currentPage < totalPage
    }
}