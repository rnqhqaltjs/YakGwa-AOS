package com.prography.yakgwa.di

import com.prography.data.repository.AuthRepositoryImpl
import com.prography.data.repository.MeetRepositoryImpl
import com.prography.data.repository.PlaceRepositoryImpl
import com.prography.data.repository.PromiseRepositoryImpl
import com.prography.data.repository.VoteRepositoryImpl
import com.prography.domain.repository.AuthRepository
import com.prography.domain.repository.MeetRepository
import com.prography.domain.repository.PlaceRepository
import com.prography.domain.repository.PromiseRepository
import com.prography.domain.repository.VoteRepository
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
}