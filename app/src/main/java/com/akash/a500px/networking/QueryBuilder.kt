package com.akash.a500px.networking

import android.net.Uri
import java.util.*
import com.google.common.base.Joiner

/**
 * Created by musheng on 2018-01-07.
 */

class QueryBuilder {

    internal var mQuery: MutableList<String> = ArrayList()

    fun add(k: String, v: String): QueryBuilder {
        if (k.isNotEmpty() && v.isNotEmpty()) {
            mQuery.add(String.format("%s=%s", k, Uri.encode(v)))
        }

        return this
    }

    fun add(k: String, v: Int): QueryBuilder {
        val vString = v.toString()
        mQuery.add(String.format("%s=%s", k, Uri.encode(vString)))
        return this
    }

    fun add(k: String, v: Boolean): QueryBuilder {
        val vString = v.toString()
        mQuery.add(String.format("%s=%s", k, Uri.encode(vString)))
        return this
    }

    fun add(k: String, v: List<String>?): QueryBuilder {
        if (k.isNotEmpty() && v != null && v.isNotEmpty()) {
            mQuery.add(String.format("%s=%s", k, Joiner.on(",").join(v)))
        }

        return this
    }

    fun add(k: String, v: Array<String>?): QueryBuilder {
        if (k.isNotEmpty() && v != null && v.isNotEmpty()) {
            mQuery.add(String.format("%s=%s", k, Joiner.on(",").join(v)))
        }

        return this
    }

    fun build(): String {
        return Joiner.on("&").join(mQuery)
    }
}