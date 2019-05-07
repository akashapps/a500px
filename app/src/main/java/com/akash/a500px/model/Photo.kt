package com.akash.a500px.model

import com.akash.a500px.networking.Config
import org.json.JSONObject

class Photo {

    var id: Int = 0
    var imageUrl: String = ""
    var height: Int = 0
    var width: Int = 0
    var imageRatio: Float = 0f
    var column: Int = 1

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

            if (photoList.size == 0){
                return photoList
            }

            // update coulumn

            val result = ArrayList<Photo>()
            var row = ArrayList<Photo>()
            var rowRatio = 0f

            for (photo in photoList){
                var ratio = photo.width / photo.height.toFloat()

                photo.imageRatio = ratio

                result.add(photo)
                rowRatio += ratio

                if (rowRatio > 2f){
                    var used = 0

                    for (photoRow in row){
                        photoRow.column = ((Config.NUMBER_OF_COLUMN * photoRow.column) / rowRatio).toInt()
                        used += photoRow.column
                    }

                    photo.column = Config.NUMBER_OF_COLUMN - used

                    row.clear()
                    rowRatio = 0f

                }else{
                    row.add(photo)
                }
            }
            return result
        }
    }
}