package com.prography.yakgwa.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.prography.data.local.PromiseDatabase
import com.prography.data.local.typeconverter.PlaceInfoTypeConverter
import com.prography.data.local.typeconverter.TimeInfoTypeConverter
import com.prography.data.local.typeconverter.UserInfoTypeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun providesPromiseDatabase(@ApplicationContext context: Context, gson: Gson): PromiseDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                PromiseDatabase::class.java,
                "promise_database"
            )
            .addTypeConverter(UserInfoTypeConverter(gson))
            .addTypeConverter(TimeInfoTypeConverter(gson))
            .addTypeConverter(PlaceInfoTypeConverter(gson))
            .build()

    @Singleton
    @Provides
    fun providesPromiseDao(promiseDatabase: PromiseDatabase) = promiseDatabase.promiseDao()
}