package com.akash.a500px.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

class User (): Parcelable {
    var firstName = ""
    var lastName = ""
    var avatar = ""

    constructor(jsonObject: JSONObject): this() {
        firstName = jsonObject.getString("firstname")
        lastName = jsonObject.getString("lastname")
        avatar = jsonObject.getString("userpic_url")
    }

    constructor(source: Parcel) : this(){
        firstName = source.readString()
        lastName = source.readString()
        avatar = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(avatar)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}