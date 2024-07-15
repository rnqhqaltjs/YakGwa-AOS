package com.prography.yakgwa.di

import com.prography.data.repository.AuthRepositoryImpl
import com.prography.data.repository.MeetRepositoryImpl
import com.prography.data.repository.NaverRepositoryImpl
import com.prography.data.repository.VoteRepositoryImpl
import com.prography.domain.repository.AuthRepository
import com.prography.domain.repository.MeetRepository
import com.prography.domain.repository.NaverRepository
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
    abstract fun bindsNaverRepository(naverRepositoryImpl: NaverRepositoryImpl): NaverRepository

}