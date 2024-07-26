package com.prography.yakgwa.ui.vote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentAddCandidatePlaceVoteBinding
import com.prography.yakgwa.model.SelectedLocationModel
import com.prography.yakgwa.ui.createPromise.createPromisePlace.CandidateLocationDetailListAdapter
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import com.prography.yakgwa.util.extensions.RESULT_KEY
import com.prography.yakgwa.util.extensions.setNavResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCandidatePlaceVoteFragment :
    BaseFragment<FragmentAddCandidatePlaceVoteBinding>(R.layout.fragment_add_candidate_place_vote) {
    private val viewModel: VoteViewModel by viewModels()
    private lateinit var candidateLocationDetailListAdapter: CandidateLocationDetailListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun setupRecyclerView() {
        candidateLocationDetailListAdapter = CandidateLocationDetailListAdapter().apply {
            setOnItemClickListener { position ->
                viewModel.singleCandidateLocationSelection(position)
            }
        }
        binding.rvSelectedLocation.adapter = candidateLocationDetailListAdapter
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedCandidateLocationState.collectLatest { selectedLocation ->
                candidateLocationDetailListAdapter.submitList(selectedLocation)
                binding.btnConfirm.isEnabled = !selectedLocation.none { it.isSelected }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.candidateLocationState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            candidateLocationDetailListAdapter.submitList(
                                it.data.map { locationItem ->
                                    SelectedLocationModel(locationItem)
                                })
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
                viewModel.addPlaceState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            setNavResult(RESULT_KEY, true)
                            navigateToVotePromisePlaceFragment()
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
        binding.svSearchLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getCandidateLocations(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.btnConfirm.setOnClickListener {
            viewModel.addPlaceCandidate()
        }

        binding.ivCloseBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateToVotePromisePlaceFragment() {
        AddCandidatePlaceVoteFragmentDirections.actionAddCandidatePlaceVoteFragmentToVotePromisePlaceFragment(
            viewModel.meetId
        ).apply {
            findNavController().navigate(this)
        }
    }
}