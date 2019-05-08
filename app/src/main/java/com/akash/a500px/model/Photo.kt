package com.akash.a500px.model

import android.os.Parcel
import android.os.Parcelable
import com.akash.a500px.helper.DateFunctionality
import com.akash.a500px.networking.Config
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Photo() : Parcelable {
    var id: Int = 0

    var imageUrl: String = ""
    var imageFormat: String = ""
    var name: String = ""
    var createdAt = Date()

    var height: Int = 0

    var width: Int = 0

    var imageRatio: Float = 0f

    var column: Int = 1

    var user: User? = null

    // *********************************************************************************************
    // region Constructor

    constructor(jsonObject: JSONObject) : this() {
        id = jsonObject.getInt("id")
        height = jsonObject.getInt("height")
        width = jsonObject.getInt("width")

        imageUrl = jsonObject.getJSONArray("image_url")[0].toString()

        imageFormat = jsonObject.getString("image_format")
        name = jsonObject.getString("name")

        user = User(jsonObject.getJSONObject("user"))

        val createAtString = jsonObject.getString("created_at")
        createdAt = DateFunctionality.getDateFromStringFormat(createAtString)

    }

    // endregion

    // *********************************************************************************************
    // region Utility

    fun isActiveObject(): Boolean {
        return id > 0 && imageUrl.isNotBlank()
    }

    fun isGifImage(): Boolean{
        return imageFormat.toLowerCase().equals("gif")
    }

    // endregion

    // *********************************************************************************************
    // region Parcel

    constructor(source: Parcel) : this(){
        id = source.readInt()
        imageUrl = source.readString()
        height = source.readInt()
        width = source.readInt()
        imageFormat = source.readString()
        name = source.readString()
        user = source.readParcelable(User::class.java.classLoader)
        createdAt = Date(source.readLong())
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        dest.writeInt(id)
        dest.writeString(imageUrl)
        dest.writeInt(height)
        dest.writeInt(width)
        dest.writeString(imageFormat)
        dest.writeString(name)
        dest.writeParcelable(user, flags)
        dest.writeLong(createdAt.time)
    }

    // endregion

    companion object {
        fun factoryList(jsonObject: JSONObject?): ArrayList<Photo> {
            val photoList = ArrayList<Photo>()

            if (jsonObject == null) {
                return photoList
            }

            val jsonArray = jsonObject.getJSONArray("photos")

            for (i in 0 until jsonArray.length()) {
                val photoJSON = jsonArray[i]

                if (photoJSON is JSONObject == false) {
                    continue
                }

                val photo = Photo(photoJSON)

                if (photo.isActiveObject()) {
                    photoList.add(photo)
                }
            }

            if (photoList.size == 0) {
                return photoList
            }

            // update coulumn

            val result = ArrayList<Photo>()
            val row = ArrayList<Photo>()
            var rowRatio = 0f

            for (photo in photoList) {
                var ratio = photo.width / photo.height.toFloat()

                photo.imageRatio = ratio

                result.add(photo)
                rowRatio += ratio

                if (rowRatio > 2f) {
                    var used = 0

                    for (photoRow in row) {
                        photoRow.column = ((Config.NUMBER_OF_COLUMN * photoRow.column) / rowRatio).toInt()
                        used += photoRow.column
                    }

                    photo.column = Config.NUMBER_OF_COLUMN - used

                    row.clear()
                    rowRatio = 0f

                } else {
                    row.add(photo)
                }
            }
            return result
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Photo> = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(source: Parcel): Photo = Photo(source)
            override fun newArray(size: Int): Array<Photo?> = arrayOfNulls(size)
        }
    }
}