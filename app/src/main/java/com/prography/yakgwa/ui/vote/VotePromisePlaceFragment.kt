package com.prography.yakgwa.ui.vote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentVotePromisePlaceBinding
import com.prography.yakgwa.model.PlaceModel
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VotePromisePlaceFragment :
    BaseFragment<FragmentVotePromisePlaceBinding>(R.layout.fragment_vote_promise_place) {

    private val viewModel: VoteViewModel by viewModels()
    private lateinit var placeListAdapter: PlaceListAdapter

    private val args by navArgs<VotePromisePlaceFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meetId = args.meetId

        initView(meetId)
        setupRecyclerView()
        observer(meetId)
        addListeners(meetId)
    }

    private fun initView(meetId: Int) {
        viewModel.getVotePlaceCandidate(meetId)
    }

    private fun setupRecyclerView() {
        placeListAdapter = PlaceListAdapter().apply {
            setOnItemClickListener { position ->
                viewModel.singlePlaceSelection(position)
            }
        }
        binding.rvPlace.adapter = placeListAdapter
    }

    private fun observer(meetId: Int) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.placeCandidateState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            placeListAdapter.submitList(
                                it.data.map { placeItem ->
                                    PlaceModel(placeItem)
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
                viewModel.placeVoteState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> navigateToInvitationLeaderFragment(meetId)
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

    private fun addListeners(meetId: Int) {
        binding.btnVoteComplete.setOnClickListener {
            viewModel.votePlace(meetId)
        }
        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateToInvitationLeaderFragment(meetId: Int) {
        VotePromisePlaceFragmentDirections.actionVotePromisePlaceFragmentToInvitationLeaderFragment(
            meetId
        ).apply {
            findNavController().navigate(this)
        }
    }
}