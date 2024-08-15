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
import com.prography.domain.model.response.MeetDetailResponseEntity
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationMemberBinding
import com.prography.yakgwa.type.RoleType
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
                            showMeetDetails(it.data)
                            viewModel.setParticipantInfo(it.data.participantInfo)
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
            repeatOnLifecycle(Lifecycle.State.CREATED) {
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

        binding.tvShowEntire.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.invitationMemberFragment) {
                navigateToParticipantMemberFragment()
            }
        }

        binding.ivCloseBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showMeetDetails(item: MeetDetailResponseEntity) {
        with(binding) {
            tvTemaName.text = item.meetInfo.themeName
            tvInvitationTitle.text = item.meetInfo.meetTitle
            tvInvitationDescription.text = item.meetInfo.description
            tvParticipantDescription.text =
                "${item.participantInfo.find { it.meetRole == RoleType.LEADER.name }?.name}님이 약속에 초대했어요"
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

    private fun navigateToParticipantMemberFragment() {
        InvitationMemberFragmentDirections.actionInvitationMemberFragmentToParticipantMemberFragment(
            viewModel.participantInfo.value
        ).apply {
            findNavController().navigate(this)
        }
    }
}