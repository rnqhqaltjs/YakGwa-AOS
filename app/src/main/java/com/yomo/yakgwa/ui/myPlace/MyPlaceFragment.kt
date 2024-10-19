package com.yomo.yakgwa.ui.myPlace

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yomo.domain.model.request.MyPlaceRequestEntity
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentMyPlaceBinding
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPlaceFragment : BaseFragment<FragmentMyPlaceBinding>(R.layout.fragment_my_place) {
    private val viewModel: MyPlaceViewModel by viewModels()

    private lateinit var myPlaceListAdapter: MyPlaceListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.locationState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            myPlaceListAdapter.submitList(it.data)
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
                viewModel.myPlaceState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            val message = if (it.data) {
                                "나의 장소에 등록되었어요."
                            } else {
                                "나의 장소에서 삭제되었어요."
                            }
                            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.toggleState.collect { updatedList ->
                myPlaceListAdapter.submitList(updatedList)
            }
        }
    }

    private fun setupRecyclerView() {
        myPlaceListAdapter = MyPlaceListAdapter().apply {
            setOnToggleClickListener { item, state ->
                viewModel.myPlace(
                    MyPlaceRequestEntity(
                        item.title,
                        item.mapx,
                        item.mapy
                    ),
                    state
                )
            }
        }
        binding.rvMyPlace.adapter = myPlaceListAdapter
    }

    private fun addListeners() {
        binding.svSearchLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getLocations(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}