package com.yomo.yakgwa.ui.vote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentVotePromisePlaceBinding
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import com.yomo.yakgwa.util.extensions.getNavResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VotePromisePlaceFragment :
    BaseFragment<FragmentVotePromisePlaceBinding>(R.layout.fragment_vote_promise_place) {
    private val viewModel: VoteViewModel by viewModels()
    private lateinit var placeListAdapter: PlaceListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
        setupSavedStateHandleObserver()
    }

    private fun setupRecyclerView() {
        placeListAdapter = PlaceListAdapter().apply {
            setOnItemClickListener { position ->
                viewModel.singlePlaceSelection(position)
            }
        }
        binding.rvPlace.adapter = placeListAdapter
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.placeCandidateState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            placeListAdapter.submitList(it.data)
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
                viewModel.placeVoteState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> navigateToInvitationLeaderFragment()
                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.selectedPlaceState.collectLatest { selectedPlace ->
                placeListAdapter.submitList(selectedPlace)
                binding.btnVoteComplete.isEnabled = !selectedPlace.none { it.isSelected }
            }
        }
    }

    private fun addListeners() {
        binding.btnVoteComplete.setOnClickListener {
            viewModel.votePlace()
        }
        binding.cvAddCandidate.setOnClickListener {
            navigateToAddCandidatePlaceVoteFragment()
        }
        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSavedStateHandleObserver() {
        getNavResult<Boolean>(R.id.votePromisePlaceFragment) { result ->
            if (result == true) {
                viewModel.getVotePlaceCandidate()
            }
        }
    }

    private fun navigateToInvitationLeaderFragment() {
        if (findNavController().currentDestination?.id == R.id.votePromisePlaceFragment) {
            VotePromisePlaceFragmentDirections.actionVotePromisePlaceFragmentToInvitationLeaderFragment(
                viewModel.meetId
            ).apply {
                findNavController().navigate(this)
            }
        }
    }

    private fun navigateToAddCandidatePlaceVoteFragment() {
        VotePromisePlaceFragmentDirections.actionVotePromisePlaceFragmentToAddCandidatePlaceVoteFragment(
            viewModel.meetId
        ).apply {
            findNavController().navigate(this)
        }
    }
}