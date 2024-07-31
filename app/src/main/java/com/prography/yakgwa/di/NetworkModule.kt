package com.prography.yakgwa.di

import com.prography.data.interceptor.AuthInterceptor
import com.prography.yakgwa.BuildConfig.BASE_URL
import com.prography.yakgwa.BuildConfig.NAVER_API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class YAKGWA

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NAVER

    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        isLenient = true
        prettyPrint = true
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    @YAKGWA
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        interceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(interceptor: AuthInterceptor): Interceptor = interceptor

    @YAKGWA
    @Singleton
    @Provides
    fun provideRetrofit(@YAKGWA okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @NAVER
    @Singleton
    @Provides
    fun provideNaverOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @NAVER
    @Singleton
    @Provides
    fun provideNaverRetrofit(@NAVER okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(NAVER_API_URL)
            .build()
    }
}