package com.yomo.yakgwa.di

import com.yomo.data.repository.AuthRepositoryImpl
import com.yomo.data.repository.MeetRepositoryImpl
import com.yomo.data.repository.NaverRepositoryImpl
import com.yomo.data.repository.PlaceRepositoryImpl
import com.yomo.data.repository.PromiseRepositoryImpl
import com.yomo.data.repository.VoteRepositoryImpl
import com.yomo.domain.repository.AuthRepository
import com.yomo.domain.repository.MeetRepository
import com.yomo.domain.repository.NaverRepository
import com.yomo.domain.repository.PlaceRepository
import com.yomo.domain.repository.PromiseRepository
import com.yomo.domain.repository.VoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsMeetRepository(meetRepositoryImpl: MeetRepositoryImpl): MeetRepository

    @Binds
    @Singleton
    abstract fun bindsVoteRepository(voteRepositoryImpl: VoteRepositoryImpl): VoteRepository

    @Binds
    @Singleton
    abstract fun bindsPlaceRepository(placeRepositoryImpl: PlaceRepositoryImpl): PlaceRepository

    @Binds
    @Singleton
    abstract fun bindsPromiseRepository(promiseRepositoryImpl: PromiseRepositoryImpl): PromiseRepository

    @Binds
    @Singleton
    abstract fun bindsNaverRepository(naverRepositoryImpl: NaverRepositoryImpl): NaverRepository
}