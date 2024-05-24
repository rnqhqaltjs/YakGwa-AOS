package com.prography.yakgwa.di

import com.prography.domain.repository.MeetRepository
import com.prography.domain.usecase.GetThemeListUseCase
import com.prography.domain.usecase.PostNewMeetCreateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun providesGetThemeListUseCase(meetRepository: MeetRepository): GetThemeListUseCase =
        GetThemeListUseCase(meetRepository = meetRepository)

    @Provides
    @Singleton
    fun providesPostNewMeetCreateUseCase(meetRepository: MeetRepository): PostNewMeetCreateUseCase =
        PostNewMeetCreateUseCase(meetRepository)
}