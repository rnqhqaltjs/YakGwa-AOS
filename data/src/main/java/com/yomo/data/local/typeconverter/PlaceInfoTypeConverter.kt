package com.yomo.data.local.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.yomo.data.model.PromiseEntity.PlaceInfo

@ProvidedTypeConverter
class PlaceInfoTypeConverter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: PlaceInfo): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): PlaceInfo {
        return gson.fromJson(value, PlaceInfo::class.java)
    }
}