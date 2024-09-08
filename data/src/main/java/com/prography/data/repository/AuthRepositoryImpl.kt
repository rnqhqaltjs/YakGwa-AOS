package com.prography.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.prography.data.datasource.remote.AuthRemoteDataSource
import com.prography.data.mapper.AuthMapper
import com.prography.domain.model.request.AuthRequestEntity
import com.prography.domain.model.response.AuthResponseEntity
import com.prography.domain.model.response.UserInfoResponseEntity
import com.prography.domain.repository.AuthRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun postLogin(
        kakaoAccessToken: String,
        authRequestEntity: AuthRequestEntity
    ): ApiResponse<AuthResponseEntity> {
        val response = authRemoteDataSource.login(
            kakaoAccessToken,
            AuthMapper.mapperToRequestAuthDto(authRequestEntity)
        )

        return response.mapSuccess {
            AuthMapper.mapperToAuthResponseEntity(this.result)
        }
    }

    override suspend fun logout(): ApiResponse<Unit> {
        return authRemoteDataSource.logout()
    }

    override suspend fun getUserInfo(): ApiResponse<UserInfoResponseEntity> {
        val response = authRemoteDataSource.getUserInfo()

        return response.mapSuccess {
            AuthMapper.mapperToUserInfoResponseEntity(this.result)
        }
    }

    override suspend fun updateUserImage(imageUri: Uri): ApiResponse<Unit> {
        return authRemoteDataSource.updateUserImage(
            MultipartBody.Part.createFormData(
                "image",
                makeImageFile(imageUri).name,
                makeImageFile(imageUri).asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
        )
    }

    override suspend fun updateFcmToken(newFcmToken: String): ApiResponse<Unit> {
        return authRemoteDataSource.updateFcmToken(newFcmToken)
    }

    override suspend fun signout(): ApiResponse<Unit> {
        return authRemoteDataSource.signout()
    }

    private fun makeImageFile(uri: Uri): File {
        // 파일 스트림으로 uri로 접근해 비트맵을 디코딩
        val bitmap = context.contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it)
        }

        // 캐시 파일 생성
        val tempFile = File.createTempFile("fileName", ".jpeg", context.cacheDir)

        // 파일 스트림을 통해 파일에 비트맵 저장
        FileOutputStream(tempFile).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return tempFile
    }
}
