package com.prography.yakgwa.ui.vote

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
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentVoteCompletionBinding
import com.prography.yakgwa.model.ConfirmPlaceModel
import com.prography.yakgwa.model.ConfirmTimeModel
import com.prography.yakgwa.type.MeetType
import com.prography.yakgwa.ui.invitation.ParticipantMemberListAdapter
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanDate
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanTime
import com.prography.yakgwa.util.OverlapDecoration
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VoteCompletionFragment :
    BaseFragment<FragmentVoteCompletionBinding>(R.layout.fragment_vote_completion) {
    private val viewModel: VoteViewModel by viewModels()

    private lateinit var participantMemberListAdapter: ParticipantMemberListAdapter
    private lateinit var confirmPlaceListAdapter: ConfirmPlaceListAdapter
    private lateinit var confirmTimeListAdapter: ConfirmTimeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedConfirmPlaceState.collectLatest { selectedPlace ->
                confirmPlaceListAdapter.submitList(selectedPlace)
            }
        }

        lifecycleScope.launch {
            viewModel.selectedConfirmTimeState.collectLatest { selectedTime ->
                confirmTimeListAdapter.submitList(selectedTime)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.timeCandidateState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            handleTimeCandidate(it.data)
                            confirmTimeListAdapter.submitList(
                                it.data.timeInfos?.map { timeInfo ->
                                    ConfirmTimeModel(timeInfo)
                                }
                            )
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
                viewModel.votePlaceInfoState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            handleVotePlaceInfo(it.data)
                            confirmPlaceListAdapter.submitList(
                                it.data.placeInfos?.map { placeInfo ->
                                    ConfirmPlaceModel(placeInfo)
                                }
                            )
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
                viewModel.confirmPlaceState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Snackbar.make(requireView(), "장소가 확정되었습니다.", Snackbar.LENGTH_SHORT)
                                .show()
                            viewModel.getUserVotePlace()
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
                viewModel.confirmTimeState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Snackbar.make(requireView(), "시간이 확정되었습니다.", Snackbar.LENGTH_SHORT)
                                .show()
                            viewModel.getVoteTimeCandidate()
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

        confirmPlaceListAdapter = ConfirmPlaceListAdapter().apply {
            setOnItemClickListener {
                viewModel.singleConfirmPlaceSelection(it)
            }
        }
        binding.rvVotePlaceResult.adapter = confirmPlaceListAdapter

        confirmTimeListAdapter = ConfirmTimeListAdapter().apply {
            setOnItemClickListener {
                viewModel.singleConfirmTimeSelection(it)
            }
        }
        binding.rvVoteTimeResult.adapter = confirmTimeListAdapter
    }

    private fun handleTimeCandidate(timeInfo: TimeCandidateResponseEntity) {
        when (timeInfo.meetStatus) {
            MeetType.BEFORE_CONFIRM.name -> {
                binding.cvPromiseTime.visibility = View.GONE
                binding.cvVoteTimeResult.visibility = View.VISIBLE
            }

            MeetType.CONFIRM.name -> {
                binding.cvPromiseTime.visibility = View.VISIBLE
                binding.cvVoteTimeResult.visibility = View.GONE
                binding.tvPromiseDate.text =
                    formatDateTimeToKoreanDate(timeInfo.timeInfos?.get(0)!!.voteTime)
                binding.tvPromiseTime.text =
                    formatDateTimeToKoreanTime(timeInfo.timeInfos?.get(0)!!.voteTime)
            }
        }
    }

    private fun handleVotePlaceInfo(voteInfo: VotePlaceResponseEntity) {
        when (voteInfo.meetStatus) {
            MeetType.BEFORE_CONFIRM.name -> {
                binding.cvPromisePlace.visibility = View.GONE
                binding.cvVotePlaceResult.visibility = View.VISIBLE
            }

            MeetType.CONFIRM.name -> {
                binding.cvPromisePlace.visibility = View.VISIBLE
                binding.cvVotePlaceResult.visibility = View.GONE
                binding.tvTitle.text = voteInfo.placeInfos?.get(0)!!.title
                binding.tvAddress.text = voteInfo.placeInfos?.get(0)!!.roadAddress
            }
        }
    }

    private fun showMeetDetails(meetInfo: MeetInfo) {
        binding.tvTemaName.text = meetInfo.themeName
        binding.tvInvitationTitle.text = meetInfo.meetTitle
    }

    private fun addListeners() {
        binding.btnConfirmPlace.setOnClickListener {
            viewModel.confirmMeetPlace()
        }

        binding.btnConfirmTime.setOnClickListener {
            viewModel.confirmMeetTime()
        }

        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}