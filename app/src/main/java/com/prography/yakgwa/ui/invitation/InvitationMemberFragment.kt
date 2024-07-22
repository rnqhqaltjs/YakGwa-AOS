package com.prography.yakgwa.ui.invitation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.prography.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationMemberBinding
import com.prography.yakgwa.ui.invitation.InvitationViewModel.Companion.INVALID_MEET_ID
import com.prography.yakgwa.util.OverlapDecoration
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InvitationMemberFragment :
    BaseFragment<FragmentInvitationMemberBinding>(R.layout.fragment_invitation_member) {
    private val viewModel: InvitationViewModel by viewModels()
    private lateinit var participantMemberListAdapter: ParticipantMemberListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun initView() {
        if (viewModel.meetId == INVALID_MEET_ID) {
            handleInvalidInvitation()
            return
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailMeetState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            showMeetDetails(it.data.meetInfo)
                            participantMemberListAdapter.submitList(it.data.participantInfo.reversed())
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
                viewModel.participantMeetState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            navigateToInvitationLeaderFragment()
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        participantMemberListAdapter = ParticipantMemberListAdapter()
        binding.rvParticipantMember.apply {
            addItemDecoration(OverlapDecoration(requireContext()))
            adapter = participantMemberListAdapter
        }
    }

    private fun addListeners() {
        binding.btnInvitationParticipant.setOnClickListener {
            viewModel.participantMeet()
        }

        binding.ivCloseBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showMeetDetails(meetInfo: MeetInfo) {
        with(binding) {
            tvTemaName.text = meetInfo.themeName
            tvInvitationTitle.text = meetInfo.meetTitle
//            tvInvitationDescription.text = meetInfo.meetDescription

//            val hours = parseHourFromTimeString(meetInfo.leftInviteTime)
//            tvInvitationEnd.text = "${hours}시간 뒤 초대 마감"
//
//            if (hours == EXPIRED_INVITATION_HOUR) {
//                handleInvalidInvitation()
//            }
        }
    }

    private fun handleInvalidInvitation() {
        findNavController().navigateUp()
        Snackbar.make(requireView(), "만료되거나 유효하지 않은 초대장입니다.", Snackbar.LENGTH_SHORT).show()
    }

    private fun navigateToInvitationLeaderFragment() {
        InvitationMemberFragmentDirections.actionInvitationMemberFragmentToInvitationLeaderFragment(
            viewModel.meetId
        )
            .apply {
                findNavController().navigate(this)
            }
    }

    companion object {
        private const val EXPIRED_INVITATION_HOUR = 0
    }
}