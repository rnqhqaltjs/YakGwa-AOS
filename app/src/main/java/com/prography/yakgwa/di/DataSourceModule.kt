package com.prography.yakgwa.di

import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.data.datasource.remote.AuthRemoteDataSource
import com.prography.data.datasource.remote.MeetRemoteDataSource
import com.prography.data.datasource.remote.NaverRemoteDataSource
import com.prography.data.datasource.remote.VoteRemoteDataSource
import com.prography.data.datasourceimpl.local.YakGwaLocalDataSourceImpl
import com.prography.data.datasourceimpl.remote.AuthRemoteDataSourceImpl
import com.prography.data.datasourceimpl.remote.MeetRemoteDataSourceImpl
import com.prography.data.datasourceimpl.remote.NaverRemoteDataSourceImpl
import com.prography.data.datasourceimpl.remote.VoteRemoteDataSourceImpl
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
    abstract fun bindsNaverRemoteDataSource(naverRemoteDataSourceImpl: NaverRemoteDataSourceImpl): NaverRemoteDataSource

}