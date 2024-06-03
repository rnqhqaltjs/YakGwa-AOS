package com.prography.yakgwa.di

import com.prography.data.service.AuthService
import com.prography.data.service.MeetService
import com.prography.data.service.NaverService
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
    fun providesNaverService(@NetworkModule.NAVER retrofit: Retrofit): NaverService =
        retrofit.create(NaverService::class.java)

}