package com.yomo.yakgwa.ui.createPromise.createPromisePlace

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentAddCandidatePlaceDetailBinding
import com.yomo.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCandidatePlaceDetailFragment :
    BaseFragment<FragmentAddCandidatePlaceDetailBinding>(R.layout.fragment_add_candidate_place_detail) {

    private val viewModel: CreatePromiseViewModel by activityViewModels()
    private lateinit var candidateLocationDetailListAdapter: CandidateLocationDetailListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetSelectedCandidateLocations()
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
            viewModel.selectedCandidateLocationDetailState.collectLatest { selectedLocation ->
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
                            candidateLocationDetailListAdapter.submitList(it.data)
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
        binding.etSearchLocation.addTextChangedListener { text ->
            viewModel.setSearchQuery(text.toString())
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.addCandidateLocation()
            findNavController().navigateUp()
        }

        binding.ivCloseBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}