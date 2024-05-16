package com.prography.yakgwa.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.common.util.Utility
import com.prography.data.service.KakaoAuthService
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.ActivityLoginBinding
import com.prography.yakgwa.ui.MainActivity
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()

        binding.kakaoLoginBtn.setOnClickListener {
            kakaoAuthService.loginKakao(viewModel::login)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect {
                    when(it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> navigateToMain()
                        is UiState.Failure -> Timber.tag(TAG).d(it.error)
                    }
                }
            }
        }
    }

    private fun navigateToMain() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }
}