package com.prography.yakgwa.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.ActivitySplashBinding
import com.prography.yakgwa.ui.MainActivity
import com.prography.yakgwa.ui.login.LoginActivity
import com.prography.yakgwa.util.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        requestPermission {
            viewModel.getDeviceToken()
            processIntent(intent)
        }
    }

    private fun processIntent(intent: Intent?) {
        if (Intent.ACTION_VIEW == intent?.action) {
            handleDeepLink(intent.data)
        } else {
            loadSplashScreen()
        }
    }

    private fun handleDeepLink(uri: Uri?) {
        uri?.let {
            val meetId = it.getQueryParameter(INVITE_ID)

            val args = Bundle().apply {
                putString(MEET_ID, meetId)
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

    private fun requestPermission(logic: () -> Unit) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    logic()
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    Toast.makeText(
                        this@SplashActivity,
                        "권한 거부\n$deniedPermissions",
                        Toast.LENGTH_SHORT
                    ).show()
                    logic()
                }
            })
            .setPermissions(
                Manifest.permission.POST_NOTIFICATIONS,
            )
            .check()
    }

    companion object {
        private const val SPLASH_SCREEN_DELAY_TIME = 500L
        private const val MEET_ID = "meetId"
        private const val INVITE_ID = "inviteId"
    }
}