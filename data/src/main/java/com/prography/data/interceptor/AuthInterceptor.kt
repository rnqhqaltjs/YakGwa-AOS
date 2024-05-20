package com.prography.data.interceptor

import android.app.Application
import android.content.Intent
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.data.model.response.BaseResponse
import com.prography.data.model.response.ReissueResponse.ResponseReissueDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val json: Json,
    private val localStorage: YakGwaLocalDataSource,
    private val context: Application
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = originalRequest.newBuilder()
            .addHeader("token", runBlocking { localStorage.accessToken.first() }).build()
        val response = chain.proceed(authRequest)

        when (response.code) {
            401 -> {
                response.close()
                val refreshTokenRequest = originalRequest.newBuilder().get()
                    .url("${BASE_URL}api/v1/auth/reissue")
                    .addHeader(AUTHORIZATION, runBlocking { localStorage.refreshToken.first() })
                    .build()
                val refreshTokenResponse = chain.proceed(refreshTokenRequest)

                if (refreshTokenResponse.isSuccessful) {
                    val responseRefresh =
                        json.decodeFromString<BaseResponse<ResponseReissueDto>>(
                            refreshTokenResponse.body?.string()
                                ?: throw IllegalStateException("\"refreshTokenResponse is null $refreshTokenResponse\"")
                        )

                    with(localStorage) {
                        CoroutineScope(Dispatchers.IO).launch {
                            saveAccessToken(BEARER + responseRefresh.result.tokenSet.accessToken)
                            saveRefreshToken(BEARER + responseRefresh.result.tokenSet.refreshToken)
                        }
                    }

                    val newRequest = originalRequest.newBuilder()
                        .addHeader("token", runBlocking { localStorage.accessToken.first() })
                        .build()
                    return chain.proceed(newRequest)
                } else {
                    with(context) {
                        CoroutineScope(Dispatchers.Main).launch {
                            startActivity(
                                Intent.makeRestartActivityTask(
                                    packageManager.getLaunchIntentForPackage(packageName)?.component
                                )
                            )
                            localStorage.clear()
                        }
                    }
                }
            }
        }
        return response
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "
        const val BASE_URL = "http://43.202.47.80:8081/"
    }
}