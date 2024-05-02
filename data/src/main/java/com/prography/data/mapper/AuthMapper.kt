package com.prography.data.mapper

import com.prography.data.model.request.RequestAuthDto
import com.prography.data.model.response.AuthResponse.ResponseAuthDto
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity

object AuthMapper {
    fun mapperToRequestAuthDto(authRequestEntity: AuthRequestEntity): RequestAuthDto {
        return authRequestEntity.run {
            RequestAuthDto(loginType)
        }
    }

    fun mapperToAuthResponseEntity(responseAuthDto: ResponseAuthDto): AuthResponseEntity {
        return responseAuthDto.run {
            AuthResponseEntity(this.result.tokenSet.accessToken, this.result.tokenSet.refreshToken, this.result.isNew)
        }
    }
}