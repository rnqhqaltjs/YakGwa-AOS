package com.yomo.yakgwa.ui.createPromise.createPromisePlace

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.yomo.domain.model.response.LocationResponseEntity
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentDirectInputPlaceBinding
import com.yomo.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class DirectInputPlaceFragment :
    BaseFragment<FragmentDirectInputPlaceBinding>(R.layout.fragment_direct_input_place) {
    private val viewModel: CreatePromiseViewModel by activityViewModels()

    private lateinit var directLocationListAdapter: DirectLocationListAdapter
    private lateinit var selectedDirectLocationListAdapter: SelectedDirectLocationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedDirectLocationState.collectLatest { selectedLocations ->
                selectedDirectLocationListAdapter.submitList(selectedLocations)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.directLocationState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            directLocationListAdapter.submitList(it.data)
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
        directLocationListAdapter = DirectLocationListAdapter().apply {
            setOnItemClickListener { selectedLocation ->
                if (selectedDirectLocationListAdapter.itemCount > MAX_SELECTED_COUNT) {
                    Snackbar.make(requireView(), "장소를 더 추가할 수 없어요.", Snackbar.LENGTH_SHORT).show()
                    binding.etSearchLocation.setText("")
                } else {
                    viewModel.addDirectLocation(
                        LocationResponseEntity(
                            LocationResponseEntity.PlaceInfoEntity(
                                selectedLocation.placeInfoEntity.title,
                                selectedLocation.placeInfoEntity.link,
                                selectedLocation.placeInfoEntity.category,
                                selectedLocation.placeInfoEntity.description,
                                selectedLocation.placeInfoEntity.telephone,
                                selectedLocation.placeInfoEntity.address,
                                selectedLocation.placeInfoEntity.roadAddress,
                                selectedLocation.placeInfoEntity.mapx,
                                selectedLocation.placeInfoEntity.mapy
                            ),
                            selectedLocation.isUserLike
                        )
                    )
                    binding.etSearchLocation.setText("")
                }
            }
        }
        binding.rvSearchLocation.adapter = directLocationListAdapter

        selectedDirectLocationListAdapter = SelectedDirectLocationListAdapter().apply {
            setOnRemoveClickListener { selectedLocation ->
                viewModel.removeDirectLocation(selectedLocation)
            }
        }
        binding.rvSelectedLocation.adapter = selectedDirectLocationListAdapter
    }

    private fun addListeners() {
        binding.etSearchLocation.addTextChangedListener { text ->
            viewModel.setSearchQuery(text.toString())
        }
    }

    companion object {
        private const val MAX_SELECTED_COUNT = 0
    }
}