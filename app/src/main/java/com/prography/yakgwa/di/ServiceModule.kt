package com.prography.yakgwa.di

import com.prography.data.remote.AuthService
import com.prography.data.remote.MeetService
import com.prography.data.remote.PlaceService
import com.prography.data.remote.VoteService
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

}