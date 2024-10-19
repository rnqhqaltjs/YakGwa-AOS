package com.yomo.yakgwa

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.yomo.yakgwa.BuildConfig.KAKAO_NATIVE_APP_KEY
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class YakGwaApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        setTimber()
        setKakao()
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setKakao() {
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
    }
}