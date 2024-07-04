package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentDirectInputPlaceBinding
import com.prography.yakgwa.model.SelectedLocationModel
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class DirectInputPlaceFragment :
    BaseFragment<FragmentDirectInputPlaceBinding>(R.layout.fragment_direct_input_place) {
    private val viewModel: CreatePromiseViewModel by viewModels()

    private lateinit var locationListAdapter: LocationListAdapter
    private lateinit var selectedLocationListAdapter: SelectedLocationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedLocationsState.collectLatest { selectedLocations ->
                selectedLocationListAdapter.submitList(selectedLocations)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            locationListAdapter.submitList(it.data)
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
        locationListAdapter = LocationListAdapter().apply {
            setOnItemClickListener { selectedLocation ->
                if (selectedLocationListAdapter.itemCount > MAX_SELECTED_COUNT) {
                    Snackbar.make(requireView(), "장소를 더 추가할 수 없어요.", Snackbar.LENGTH_SHORT).show()
                    binding.etSearchLocation.setText("")
                } else {
                    viewModel.addLocation(SelectedLocationModel(selectedLocation.title))
                    binding.etSearchLocation.setText("")
                }
            }
        }
        binding.rvSearchLocation.adapter = locationListAdapter

        selectedLocationListAdapter = SelectedLocationListAdapter().apply {
            setOnRemoveClickListener { selectedLocation ->
                viewModel.removeLocation(selectedLocation)
            }
        }
        binding.rvSelectedLocation.adapter = selectedLocationListAdapter
    }

    private fun addListeners() {
        binding.etSearchLocation.addTextChangedListener { text ->
            viewModel.setSearchQuery(text.toString())
        }
    }

    companion object {
        const val MAX_SELECTED_COUNT = 1
    }
}