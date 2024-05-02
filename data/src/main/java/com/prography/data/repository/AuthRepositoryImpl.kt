package com.prography.data.repository

import com.prography.data.datasource.remote.AuthRemoteDataSource
import com.prography.data.mapper.AuthMapper
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity
import com.prography.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun postLogin(
        kakaoAccessToken: String,
        authRequestEntity: AuthRequestEntity
    ): Result<AuthResponseEntity> {
        val response = authRemoteDataSource.login(kakaoAccessToken, AuthMapper.mapperToRequestAuthDto(authRequestEntity))

        return runCatching {
            AuthMapper.mapperToAuthResponseEntity(response)
        }
    }
}
