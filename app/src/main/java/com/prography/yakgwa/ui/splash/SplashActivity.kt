package com.prography.yakgwa.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.ActivitySplashBinding
import com.prography.yakgwa.ui.MainActivity
import com.prography.yakgwa.ui.login.LoginActivity
import com.prography.yakgwa.util.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        processIntent(intent)
    }

    private fun processIntent(intent: Intent?) {
        if (Intent.ACTION_VIEW == intent?.action) {
            handleDeepLink(intent.data)
        } else {
            loadSplashScreen()
        }
    }

    private fun handleDeepLink(uri: Uri?) {
        if (uri != null) {
            val userId = uri.getQueryParameter("userId")
            val meetId = uri.getQueryParameter("meetId")

            val args = Bundle().apply {
                putString("userId", userId)
                putString("meetId", meetId)
            }

            lifecycleScope.launch {
                delay(SPLASH_SCREEN_DELAY_TIME)
                if (viewModel.isLogin()) {
                    val pendingIntent = NavDeepLinkBuilder(this@SplashActivity)
                        .setComponentName(MainActivity::class.java)
                        .setGraph(R.navigation.main_navigation)
                        .setDestination(R.id.invitationMemberFragment)
                        .setArguments(args)
                        .createPendingIntent()

                    pendingIntent.send()
                } else {
                    navigateToLogin()
                }
            }
        }
    }

    private fun loadSplashScreen() {
        lifecycleScope.launch {
            delay(SPLASH_SCREEN_DELAY_TIME)
            if (viewModel.isLogin()) {
                navigateToMain()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun navigateToMain() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    companion object {
        const val SPLASH_SCREEN_DELAY_TIME = 500L
    }
}