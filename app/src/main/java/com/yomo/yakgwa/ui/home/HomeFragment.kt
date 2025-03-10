package com.yomo.yakgwa.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentHomeBinding
import com.yomo.yakgwa.type.MeetType
import com.yomo.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val createPromiseViewModel: CreatePromiseViewModel by activityViewModels()
    private lateinit var participantMeetListAdapter: ParticipantMeetListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPromiseViewModel.clearData()
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.meetsState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            participantMeetListAdapter.submitList(it.data.reversed()) {
                                updateNoPromiseCardVisibility()
                            }
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
        participantMeetListAdapter = ParticipantMeetListAdapter().apply {
            setOnItemClickListener {
                when (it.meetStatus) {
                    MeetType.BEFORE_VOTE.name -> navigateToInvitationLeaderFragment(it.meetInfo.meetId)
                    MeetType.VOTE.name -> navigateToInvitationLeaderFragment(it.meetInfo.meetId)
                    MeetType.BEFORE_CONFIRM.name -> navigateToVoteResultFragment(it.meetInfo.meetId)
                    MeetType.CONFIRM.name -> navigateToVoteResultFragment(it.meetInfo.meetId)
                }
            }
        }

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvParticipantMeets)
        binding.rvParticipantMeets.adapter = participantMeetListAdapter
    }

    private fun addListeners() {
        binding.btnGoCreatePromise.setOnClickListener {
            navigateToCreatePromiseTitleFragment()
        }
    }

    private fun updateNoPromiseCardVisibility() {
        if (participantMeetListAdapter.itemCount == 0) {
            binding.cvNoPromise.visibility = View.VISIBLE
        } else {
            binding.cvNoPromise.visibility = View.INVISIBLE
        }
    }

    private fun navigateToCreatePromiseTitleFragment() {
        HomeFragmentDirections.actionHomeFragmentToCreatePromiseTitleFragment().apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToInvitationLeaderFragment(meetId: Int) {
        HomeFragmentDirections.actionHomeFragmentToInvitationLeaderFragment(meetId)
            .apply {
                findNavController().navigate(this)
            }
    }

    private fun navigateToVoteResultFragment(meetId: Int) {
        HomeFragmentDirections.actionHomeFragmentToVoteResultFragment(meetId)
            .apply {
                findNavController().navigate(this)
            }
    }
}