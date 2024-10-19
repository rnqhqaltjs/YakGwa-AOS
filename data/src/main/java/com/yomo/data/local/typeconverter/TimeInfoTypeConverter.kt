package com.yomo.data.local.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.yomo.data.model.PromiseEntity.TimeInfo

@ProvidedTypeConverter
class TimeInfoTypeConverter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: TimeInfo): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): TimeInfo {
        return gson.fromJson(value, TimeInfo::class.java)
    }
}