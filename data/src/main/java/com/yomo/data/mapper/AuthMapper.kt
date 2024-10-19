package com.yomo.data.mapper

import com.yomo.data.model.request.RequestAuthDto
import com.yomo.data.model.response.ResponseAuthDto
import com.yomo.data.model.response.ResponseUserInfoDto
import com.yomo.domain.model.request.AuthRequestEntity
import com.yomo.domain.model.response.AuthResponseEntity
import com.yomo.domain.model.response.UserInfoResponseEntity

object AuthMapper {
    fun mapperToRequestAuthDto(authRequestEntity: AuthRequestEntity): RequestAuthDto {
        return authRequestEntity.run {
            RequestAuthDto(
                this.loginType,
                this.fcmToken
            )
        }
    }

    fun mapperToAuthResponseEntity(responseAuthDto: ResponseAuthDto): AuthResponseEntity {
        return responseAuthDto.run {
            AuthResponseEntity(
                this.tokenSet.accessToken,
                this.tokenSet.refreshToken,
                this.isNew,
                this.role
            )
        }
    }

    fun mapperToUserInfoResponseEntity(responseUserInfoDto: ResponseUserInfoDto): UserInfoResponseEntity {
        return responseUserInfoDto.run {
            UserInfoResponseEntity(
                this.name,
                this.imageUrl
            )
        }
    }
}