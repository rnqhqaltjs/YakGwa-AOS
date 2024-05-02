package com.prography.data.datasource.remote

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.AuthResponse.ResponseAuthDto

interface AuthRemoteDataSource {
    suspend fun login(kakaoAccessToken: String, requestAuthDto: RequestAuthDto): ResponseAuthDto
}