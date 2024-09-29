package com.yomo.yakgwa.di

import com.yomo.domain.repository.AuthRepository
import com.yomo.domain.repository.MeetRepository
import com.yomo.domain.repository.NaverRepository
import com.yomo.domain.repository.PlaceRepository
import com.yomo.domain.repository.PromiseRepository
import com.yomo.domain.repository.VoteRepository
import com.yomo.domain.usecase.AddPromiseHistoryUseCase
import com.yomo.domain.usecase.GetLocationListUseCase
import com.yomo.domain.usecase.GetMeetInformationDetailUseCase
import com.yomo.domain.usecase.GetParticipantMeetListUseCase
import com.yomo.domain.usecase.GetPlaceCandidateInfoUseCase
import com.yomo.domain.usecase.GetPromiseHistoryListUseCase
import com.yomo.domain.usecase.GetStaticMapUseCase
import com.yomo.domain.usecase.GetThemeListUseCase
import com.yomo.domain.usecase.GetUserInformationUseCase
import com.yomo.domain.usecase.GetUserVotePlaceListUseCase
import com.yomo.domain.usecase.GetVoteTimeCandidateInfoUseCase
import com.yomo.domain.usecase.PatchConfirmMeetPlaceUseCase
import com.yomo.domain.usecase.PatchConfirmMeetTimeUseCase
import com.yomo.domain.usecase.PatchUpdateUserImageUseCase
import com.yomo.domain.usecase.PostMyPlaceUseCase
import com.yomo.domain.usecase.PostNewMeetCreateUseCase
import com.yomo.domain.usecase.PostParticipantMeetUseCase
import com.yomo.domain.usecase.PostPlaceCandidateInfoUseCase
import com.yomo.domain.usecase.PostUserLoginUseCase
import com.yomo.domain.usecase.PostUserLogoutUseCase
import com.yomo.domain.usecase.PostUserSignoutUseCase
import com.yomo.domain.usecase.PostUserVotePlaceUseCase
import com.yomo.domain.usecase.PostUserVoteTimeUseCase
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

    @Provides
    @Singleton
    fun providesPostUserSignoutUseCase(authRepository: AuthRepository): PostUserSignoutUseCase =
        PostUserSignoutUseCase(authRepository)
}