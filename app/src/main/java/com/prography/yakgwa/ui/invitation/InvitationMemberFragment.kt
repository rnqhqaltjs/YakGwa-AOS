package com.prography.yakgwa.ui.invitation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.prography.domain.model.response.CreateMeetResponseEntity
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationMemberBinding
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InvitationMemberFragment :
    BaseFragment<FragmentInvitationMemberBinding>(R.layout.fragment_invitation_member) {

    private val viewModel: InvitationViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getString("userId")?.toInt()
        val meetId = arguments?.getString("meetId")?.toInt()

        initView(userId!!, meetId!!)
        observer(meetId)
        addListeners(userId, meetId)
    }

    private fun initView(userId: Int, meetId: Int) {
        lifecycleScope.launch {
            viewModel.getMeetInformationDetail(userId, meetId)
        }
    }

    private fun observer(meetId: Int) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailMeetState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            binding.tvTemaName.text = it.data.meetInfo.meetThemeName
                            binding.tvInvitationTitle.text = it.data.meetInfo.meetName
                            binding.tvInvitationDescription.text = it.data.meetInfo.meetDescription
                            binding.tvInvitationEnd.text =
                                it.data.meetInfo.leftInviteTime + "시간 뒤 초대 마감"
                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.participantMeetState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            navigateToInvitationLeaderFragment(meetId)
                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun addListeners(userId: Int?, meetId: Int?) {
        binding.btnInvitationParticipant.setOnClickListener {
            lifecycleScope.launch {
                if (userId != null && meetId != null) {
                    viewModel.participantMeet(viewModel.userId(), meetId)
                }
            }
        }
    }

    private fun navigateToInvitationLeaderFragment(meetId: Int) {
        InvitationMemberFragmentDirections.actionInvitationMemberFragmentToInvitationLeaderFragment(
            CreateMeetResponseEntity(meetId)
        )
            .apply {
                findNavController().navigate(this)
            }
    }
}