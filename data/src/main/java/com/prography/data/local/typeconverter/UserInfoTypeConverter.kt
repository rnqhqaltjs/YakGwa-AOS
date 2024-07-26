package com.prography.data.local.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.prography.data.model.PromiseEntity.UserInfo

@ProvidedTypeConverter
class UserInfoTypeConverter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: List<UserInfo>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<UserInfo> {
        return gson.fromJson(value, Array<UserInfo>::class.java).toList()
    }
}