package com.akash.a500px.model

import org.json.JSONObject

class Photo {

    var id: Int = 0
    var imageUrl: String = ""
    var height: Int = 0
    var width: Int = 0

    constructor(jsonObject: JSONObject){
        id = jsonObject.getInt("id")
        height = jsonObject.getInt("height")
        width = jsonObject.getInt("width")

        imageUrl = jsonObject.getJSONArray("image_url")[0].toString()
    }

    fun isActiveObject(): Boolean{
        return id > 0 && imageUrl.isNotBlank()
    }

    companion object{
        fun factoryList(jsonObject: JSONObject?): ArrayList<Photo>{
            val photoList = ArrayList<Photo>()

            if (jsonObject == null){
                return photoList
            }

            val jsonArray = jsonObject.getJSONArray("photos")

            for(i in 0 until jsonArray.length()){
                val photoJSON = jsonArray[i]

                if (photoJSON is JSONObject == false){
                    continue
                }

                val photo = Photo(photoJSON)

                if (photo.isActiveObject()){
                    photoList.add(photo)
                }
            }

            return photoList
        }
    }
}