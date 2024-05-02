package com.prography.yakgwa.di

import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.data.datasource.remote.AuthRemoteDataSource
import com.prography.data.datasourceimpl.local.YakGwaLocalDataSourceImpl
import com.prography.data.datasourceimpl.remote.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsYakGwaLocalDataSource(yakGwaLocalDataSourceImpl: YakGwaLocalDataSourceImpl): YakGwaLocalDataSource
    @Binds
    @Singleton
    abstract fun bindsAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

}