package com.prography.yakgwa.ui.vote

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
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
@RequiresApi(Build.VERSION_CODES.O)
class VotePromisePlaceFragment :
    BaseFragment<FragmentVotePromisePlaceBinding>(R.layout.fragment_vote_promise_place) {

    private val viewModel: VoteViewModel by activityViewModels()
    private lateinit var placeListAdapter: PlaceListAdapter

    private val args by navArgs<VotePromisePlaceFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meetId = args.meetId

        setupRecyclerView()
        initView(meetId)
        observer()
        addListeners()
    }

    private fun setupRecyclerView() {
        placeListAdapter = PlaceListAdapter().apply {
            setOnItemClickListener { position ->
                viewModel.singlePlaceSelection(position)
            }
        }
        binding.rvPlace.adapter = placeListAdapter
    }

    private fun initView(meetId: Int) {
//        viewModel.voteTime()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.timePlaceState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            placeListAdapter.submitList(
                                it.data.placeItems.map { placeItem ->
                                    PlaceModel(placeItem)
                                }
                            )
                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.selectedPlaceState.collectLatest { selectedPlace ->
                placeListAdapter.submitList(selectedPlace)
            }
        }
    }

    private fun addListeners() {
        binding.btnVoteComplete.setOnClickListener {

        }
        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}