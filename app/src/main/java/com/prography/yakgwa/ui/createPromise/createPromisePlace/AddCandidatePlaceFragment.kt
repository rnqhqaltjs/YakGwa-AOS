package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentAddCandidatePlaceBinding
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class AddCandidatePlaceFragment :
    BaseFragment<FragmentAddCandidatePlaceBinding>(R.layout.fragment_add_candidate_place) {

    private val viewModel: CreatePromiseViewModel by activityViewModels()
    private lateinit var candidateLocationListAdapter: CandidateLocationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedCandidateLocationState.collectLatest { selectedLocations ->
                candidateLocationListAdapter.submitList(selectedLocations)
            }
        }
    }

    private fun setupRecyclerView() {
        candidateLocationListAdapter = CandidateLocationListAdapter().apply {
            setOnRemoveClickListener {
                viewModel.removeCandidateLocation(it)
            }
        }
        binding.rvSelectedLocation.adapter = candidateLocationListAdapter
    }

    private fun addListeners() {
        binding.cvAddCandidate.setOnClickListener {
            if (candidateLocationListAdapter.itemCount > MAX_SELECTED_COUNT) {
                Snackbar.make(requireView(), "장소를 더 추가할 수 없어요.", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.onAddCandidateClicked()
            }
        }
    }

    companion object {
        private const val MAX_SELECTED_COUNT = 2
    }
}