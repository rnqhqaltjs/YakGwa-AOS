package com.yomo.yakgwa.di

import com.yomo.data.datasource.local.YakGwaLocalDataSource
import com.yomo.data.datasource.remote.AuthRemoteDataSource
import com.yomo.data.datasource.remote.MeetRemoteDataSource
import com.yomo.data.datasource.remote.NaverRemoteDataSource
import com.yomo.data.datasource.remote.PlaceRemoteDataSource
import com.yomo.data.datasource.remote.VoteRemoteDataSource
import com.yomo.data.datasourceimpl.local.YakGwaLocalDataSourceImpl
import com.yomo.data.datasourceimpl.remote.AuthRemoteDataSourceImpl
import com.yomo.data.datasourceimpl.remote.MeetRemoteDataSourceImpl
import com.yomo.data.datasourceimpl.remote.NaverRemoteDataSourceImpl
import com.yomo.data.datasourceimpl.remote.PlaceRemoteDataSourceImpl
import com.yomo.data.datasourceimpl.remote.VoteRemoteDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindsMeetRemoteDataSource(meetRemoteDataSourceImpl: MeetRemoteDataSourceImpl): MeetRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsVoteRemoteDataSource(voteRemoteDataSourceImpl: VoteRemoteDataSourceImpl): VoteRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsPlaceRemoteDataSource(placeRemoteDataSourceImpl: PlaceRemoteDataSourceImpl): PlaceRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsNaverRemoteDataSource(naverRemoteDataSourceImpl: NaverRemoteDataSourceImpl): NaverRemoteDataSource
}