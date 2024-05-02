package com.prography.data.datasourceimpl.remote

import com.prography.data.datasource.remote.AuthRemoteDataSource
import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.AuthResponse.ResponseAuthDto
import com.prography.data.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {
    override suspend fun login(
        kakaoAccessToken: String,
        requestAuthDto: RequestAuthDto
    ): ResponseAuthDto =
        authService.postLogin(kakaoAccessToken, requestAuthDto).data
}