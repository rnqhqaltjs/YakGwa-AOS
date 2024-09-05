package com.prography.yakgwa.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.prography.data.datasource.local.YakGwaLocalDataSource
import com.prography.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val localStorage: YakGwaLocalDataSource,
    private val authRepository: AuthRepository
) : ViewModel() {
    suspend fun isLogin(): Boolean {
        return withContext(Dispatchers.IO) {
            localStorage.isLogin.first()
        }
    }

    fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            val token = task.result
            viewModelScope.launch {
                authRepository.updateFcmToken(token)
                localStorage.saveDeviceToken(token)
            }
        })
    }
}