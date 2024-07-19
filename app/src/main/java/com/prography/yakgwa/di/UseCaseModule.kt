package com.prography.yakgwa.di

import com.prography.domain.repository.AuthRepository
import com.prography.domain.repository.MeetRepository
import com.prography.domain.repository.NaverRepository
import com.prography.domain.repository.VoteRepository
import com.prography.domain.usecase.GetLocationListUseCase
import com.prography.domain.usecase.GetMeetInformationDetailUseCase
import com.prography.domain.usecase.GetParticipantMeetListUseCase
import com.prography.domain.usecase.GetPlaceCandidateInfoUseCase
import com.prography.domain.usecase.GetThemeListUseCase
import com.prography.domain.usecase.GetUserInformationUseCase
import com.prography.domain.usecase.GetUserVotePlaceListUseCase
import com.prography.domain.usecase.GetVoteTimeCandidateInfoUseCase
import com.prography.domain.usecase.PostNewMeetCreateUseCase
import com.prography.domain.usecase.PostParticipantMeetUseCase
import com.prography.domain.usecase.PostUserVotePlaceUseCase
import com.prography.domain.usecase.PostUserVoteTimeUseCase
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
    fun providesGetLocationListUseCase(naverRepository: NaverRepository): GetLocationListUseCase =
        GetLocationListUseCase(naverRepository)

    @Provides
    @Singleton
    fun providesGetMeetDetailInformationUseCase(meetRepository: MeetRepository): GetMeetInformationDetailUseCase =
        GetMeetInformationDetailUseCase(meetRepository)

    @Provides
    @Singleton
    fun providesPostParticipantMeetUseCase(meetRepository: MeetRepository): PostParticipantMeetUseCase =
        PostParticipantMeetUseCase(meetRepository)

    @Provides
    @Singleton
    fun providesGetPlaceCandidateInfoUseCase(voteRepository: VoteRepository): GetPlaceCandidateInfoUseCase =
        GetPlaceCandidateInfoUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesGetVoteTimeCandidateInfoUseCase(voteRepository: VoteRepository): GetVoteTimeCandidateInfoUseCase =
        GetVoteTimeCandidateInfoUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesPostUserVoteTimeUseCase(voteRepository: VoteRepository): PostUserVoteTimeUseCase =
        PostUserVoteTimeUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesPostUserVotePlaceUseCase(voteRepository: VoteRepository): PostUserVotePlaceUseCase =
        PostUserVotePlaceUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesGetUserVotePlaceListUseCase(voteRepository: VoteRepository): GetUserVotePlaceListUseCase =
        GetUserVotePlaceListUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesGetUserInformationUseCase(authRepository: AuthRepository): GetUserInformationUseCase =
        GetUserInformationUseCase(authRepository)
}