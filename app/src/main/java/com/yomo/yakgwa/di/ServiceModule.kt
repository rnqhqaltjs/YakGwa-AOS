package com.yomo.yakgwa.di

import com.yomo.data.remote.AuthService
import com.yomo.data.remote.MeetService
import com.yomo.data.remote.NaverService
import com.yomo.data.remote.PlaceService
import com.yomo.data.remote.VoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providesAuthService(@NetworkModule.YAKGWA retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesMeetService(@NetworkModule.YAKGWA retrofit: Retrofit): MeetService =
        retrofit.create(MeetService::class.java)

    @Provides
    @Singleton
    fun providesVoteService(@NetworkModule.YAKGWA retrofit: Retrofit): VoteService =
        retrofit.create(VoteService::class.java)

    @Provides
    @Singleton
    fun providesPlaceService(@NetworkModule.YAKGWA retrofit: Retrofit): PlaceService =
        retrofit.create(PlaceService::class.java)

    @Provides
    @Singleton
    fun providesNaverService(@NetworkModule.NAVER retrofit: Retrofit): NaverService =
        retrofit.create(NaverService::class.java)
}