package com.prography.data.remote

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
import javax.inject.Inject

class KakaoAuthService @Inject constructor(
    @ActivityContext private val context: Context,
    private val client: UserApiClient
) {
    private val isKakaoTalkLoginAvailable: Boolean
        get() = client.isKakaoTalkLoginAvailable(context)

    fun loginKakao(
        loginListener: ((String) -> Unit)
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                loginError(error)
            } else if (token != null) loginSuccess(token, loginListener)
        }

        if (isKakaoTalkLoginAvailable) {
            client.loginWithKakaoTalk(context, callback = callback)
        } else {
            client.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun loginError(throwable: Throwable) {
        val kakaoType = if (isKakaoTalkLoginAvailable) KAKAO_TALK else KAKAO_ACCOUNT
        Timber.d("{$kakaoType}으로 로그인 실패 ${throwable.message}")
    }

    private fun loginSuccess(
        oAuthToken: OAuthToken,
        loginListener: ((String) -> Unit)
    ) {
        Timber.tag(KAKAO_ACCESS_TOKEN).d(oAuthToken.accessToken)
        client.me { user, error ->
            loginListener(oAuthToken.accessToken)
            if (error != null) {
                Timber.e("사용자 정보 요청 실패 $error")
            }
        }
    }

    companion object {
        const val KAKAO_TALK = "카카오톡"
        const val KAKAO_ACCOUNT = "카카오계정"
        const val KAKAO_ACCESS_TOKEN = "카카오 엑세스 토큰"
    }
}