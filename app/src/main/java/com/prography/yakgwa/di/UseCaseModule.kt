package com.prography.yakgwa.di

import com.prography.domain.repository.MeetRepository
import com.prography.domain.repository.NaverRepository
import com.prography.domain.usecase.GetLocationListUseCase
import com.prography.domain.usecase.GetParticipantMeetListUseCase
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
        GetThemeListUseCase(meetRepository)

    @Provides
    @Singleton
    fun providesPostNewMeetCreateUseCase(meetRepository: MeetRepository): PostNewMeetCreateUseCase =
        PostNewMeetCreateUseCase(meetRepository)

    @Provides
    @Singleton
    fun providesGetParticipantMeetListUseCase(meetRepository: MeetRepository): GetParticipantMeetListUseCase =
        GetParticipantMeetListUseCase(meetRepository)

    @Provides
    @Singleton
    fun providesGetLocationListUseCaseUseCase(naverRepository: NaverRepository): GetLocationListUseCase =
        GetLocationListUseCase(naverRepository)
}