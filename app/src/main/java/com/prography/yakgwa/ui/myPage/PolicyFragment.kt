package com.prography.yakgwa.ui.myPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentPolicyBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyFragment :
    BaseFragment<FragmentPolicyBinding>(R.layout.fragment_policy) {
    private val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingWebViewClient()

        when (viewModel.documentType) {
            "privacy" -> {
                binding.webView.loadUrl("https://www.notion.so/pieceofcookie/1d3074006a124e009e813f0c1f556a6e")
            }

            "terms" -> {
                binding.webView.loadUrl("https://www.notion.so/pieceofcookie/b73faa63e90340d69f453f838f1d29b2")
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun settingWebViewClient() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.domStorageEnabled = true
        }
    }
}