package com.prography.yakgwa.di

import com.prography.domain.repository.AuthRepository
import com.prography.domain.repository.MeetRepository
import com.prography.domain.repository.NaverRepository
import com.prography.domain.repository.PlaceRepository
import com.prography.domain.repository.PromiseRepository
import com.prography.domain.repository.VoteRepository
import com.prography.domain.usecase.AddPromiseHistoryUseCase
import com.prography.domain.usecase.GetLocationListUseCase
import com.prography.domain.usecase.GetMeetInformationDetailUseCase
import com.prography.domain.usecase.GetParticipantMeetListUseCase
import com.prography.domain.usecase.GetPlaceCandidateInfoUseCase
import com.prography.domain.usecase.GetPromiseHistoryListUseCase
import com.prography.domain.usecase.GetStaticMapUseCase
import com.prography.domain.usecase.GetThemeListUseCase
import com.prography.domain.usecase.GetUserInformationUseCase
import com.prography.domain.usecase.GetUserVotePlaceListUseCase
import com.prography.domain.usecase.GetVoteTimeCandidateInfoUseCase
import com.prography.domain.usecase.PatchConfirmMeetPlaceUseCase
import com.prography.domain.usecase.PatchConfirmMeetTimeUseCase
import com.prography.domain.usecase.PatchUpdateUserImageUseCase
import com.prography.domain.usecase.PostMyPlaceUseCase
import com.prography.domain.usecase.PostNewMeetCreateUseCase
import com.prography.domain.usecase.PostParticipantMeetUseCase
import com.prography.domain.usecase.PostPlaceCandidateInfoUseCase
import com.prography.domain.usecase.PostUserLoginUseCase
import com.prography.domain.usecase.PostUserLogoutUseCase
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
    fun providesPostUserLoginUseCase(authRepository: AuthRepository): PostUserLoginUseCase =
        PostUserLoginUseCase(authRepository)

    @Provides
    @Singleton
    fun providesPostUserLogoutUseCase(authRepository: AuthRepository): PostUserLogoutUseCase =
        PostUserLogoutUseCase(authRepository)

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
    fun providesGetLocationListUseCase(placeRepository: PlaceRepository): GetLocationListUseCase =
        GetLocationListUseCase(placeRepository)

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

    @Provides
    @Singleton
    fun providesPatchConfirmMeetTimeUseCase(voteRepository: VoteRepository): PatchConfirmMeetTimeUseCase =
        PatchConfirmMeetTimeUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesPatchConfirmMeetPlaceUseCase(voteRepository: VoteRepository): PatchConfirmMeetPlaceUseCase =
        PatchConfirmMeetPlaceUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesAddPromiseHistoryUseCase(promiseRepository: PromiseRepository): AddPromiseHistoryUseCase =
        AddPromiseHistoryUseCase(promiseRepository)

    @Provides
    @Singleton
    fun providesGetPromiseHistoryListUseCase(meetRepository: MeetRepository): GetPromiseHistoryListUseCase =
        GetPromiseHistoryListUseCase(meetRepository)

    @Provides
    @Singleton
    fun providesPatchUpdateUserImageUseCase(authRepository: AuthRepository): PatchUpdateUserImageUseCase =
        PatchUpdateUserImageUseCase(authRepository)

    @Provides
    @Singleton
    fun providesPostPlaceCandidateInfoUseCase(voteRepository: VoteRepository): PostPlaceCandidateInfoUseCase =
        PostPlaceCandidateInfoUseCase(voteRepository)

    @Provides
    @Singleton
    fun providesPostMyPlaceUseCase(placeRepository: PlaceRepository): PostMyPlaceUseCase =
        PostMyPlaceUseCase(placeRepository)

    @Provides
    @Singleton
    fun providesGetStaticMapUseCase(naverRepository: NaverRepository): GetStaticMapUseCase =
        GetStaticMapUseCase(naverRepository)
}