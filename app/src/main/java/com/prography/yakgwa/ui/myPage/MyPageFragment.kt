package com.prography.yakgwa.ui.myPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentMyPageBinding
import com.prography.yakgwa.ui.login.LoginActivity
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        addListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfoState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            binding.tvUserName.text = "${it.data.name}님"
                            binding.ivProfileImage.load(it.data.imageUrl)
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logoutState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            navigateToAuth()
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun addListeners() {
        binding.tvLogoutBtn.setOnClickListener {
            viewModel.logout()
        }

        binding.cvPromiseHistory.setOnClickListener {
            navigateToPromiseHistoryFragment()
        }
    }

    private fun navigateToAuth() {
        Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    private fun navigateToPromiseHistoryFragment() {
        MyPageFragmentDirections.actionMyPageFragmentToPromiseHistoryFragment().apply {
            findNavController().navigate(this)
        }
    }
}