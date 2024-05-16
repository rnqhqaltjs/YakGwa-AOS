package com.prography.yakgwa.ui.splash

import androidx.lifecycle.ViewModel
import com.prography.data.datasource.local.YakGwaLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val localStorage: YakGwaLocalDataSource
) : ViewModel() {

    suspend fun isLogin(): Boolean {
        return withContext(Dispatchers.IO) {
            localStorage.isLogin.first()
        }
    }
}