package com.prography.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prography.data.local.typeconverter.PlaceInfoTypeConverter
import com.prography.data.local.typeconverter.TimeInfoTypeConverter
import com.prography.data.local.typeconverter.UserInfoTypeConverter
import com.prography.data.model.PromiseEntity

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