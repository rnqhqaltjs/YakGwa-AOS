package com.prography.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentVoteCompletionBinding
import com.prography.yakgwa.ui.invitation.ParticipantMemberListAdapter
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VoteCompletionFragment :
    BaseFragment<FragmentVoteCompletionBinding>(R.layout.fragment_vote_completion) {
    private val viewModel: VoteViewModel by viewModels()
    private val args by navArgs<VoteCompletionFragmentArgs>()

    private lateinit var participantMemberListAdapter: ParticipantMemberListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meetId = args.meetId

        initView(meetId)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun initView(meetId: Int) {
        viewModel.getVoteTimeCandidate(meetId)
        viewModel.getUserVotePlace(meetId)
        viewModel.getMeetInformationDetail()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timeVoteState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Log.d("okay", it.data.toString())
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
                viewModel.votePlaceInfoState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Log.d("okay", it.data.placeInfos.toString())
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
                            participantMemberListAdapter.submitList(it.data.participantInfo)
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun Context.dpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun setupRecyclerView() {
        participantMemberListAdapter = ParticipantMemberListAdapter()
        binding.rvParticipantMember.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position != 0) {
                    outRect.left = requireContext().dpToPx(10f) * -1
                }
            }
        })
        binding.rvParticipantMember.adapter = participantMemberListAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun showMeetDetails(meetInfo: MeetInfo) {
        binding.tvTemaName.text = meetInfo.themeName
        binding.tvInvitationTitle.text = meetInfo.meetTitle
    }

    private fun addListeners() {
        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}