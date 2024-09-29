package com.yomo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yomo.data.local.typeconverter.PlaceInfoTypeConverter
import com.yomo.data.local.typeconverter.TimeInfoTypeConverter
import com.yomo.data.local.typeconverter.UserInfoTypeConverter
import com.yomo.data.model.PromiseEntity

@Database(
    entities = [PromiseEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        UserInfoTypeConverter::class,
        TimeInfoTypeConverter::class,
        PlaceInfoTypeConverter::class
    ]
)
abstract class PromiseDatabase : RoomDatabase() {
    abstract fun promiseDao(): PromiseDao
}