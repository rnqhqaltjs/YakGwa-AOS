package com.yomo.yakgwa.ui.myPage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentMyPageBinding
import com.yomo.yakgwa.ui.login.LoginActivity
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signoutState.collect {
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userImageState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Snackbar.make(requireView(), "이미지 변경 완료", Toast.LENGTH_SHORT).show()
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

        binding.tvWithdrawBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("알림")
                .setMessage("정말 탈퇴하시겠습니까?")
                .setPositiveButton(
                    "확인"
                ) { _, _ ->
                    viewModel.signout()
                }
                .setNegativeButton(
                    "취소"
                ) { _, _ ->
                }
            builder.show()
        }

        binding.cvPromiseHistory.setOnClickListener {
            navigateToPromiseHistoryFragment()
        }

        binding.cvMyPlace.setOnClickListener {
            navigateToMyPlaceFragment()
        }

        binding.ivProfileEdit.setOnClickListener {
            launchNewPhotoPicker()
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            navigateToPolicyFragment("privacy")
        }

        binding.tvTermsOfUse.setOnClickListener {
            navigateToPolicyFragment("terms")
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

    private fun navigateToMyPlaceFragment() {
        MyPageFragmentDirections.actionMyPageFragmentToMyPlaceFragment().apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToPolicyFragment(documentType: String) {
        MyPageFragmentDirections.actionMyPageFragmentToPolicyFragment(documentType).apply {
            findNavController().navigate(this)
        }
    }

    private val newPiker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                Timber.tag("PhotoPicker").d("Selected URI: $it")
                viewModel.updateUserImage(it)
            } ?: run {
                Timber.tag("PhotoPicker").d("No media selected")
            }
        }

    private fun launchNewPhotoPicker() {
        newPiker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}