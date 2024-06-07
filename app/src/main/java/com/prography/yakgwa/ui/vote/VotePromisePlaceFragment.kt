package com.prography.yakgwa.ui.vote

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentVotePromisePlaceBinding
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class VotePromisePlaceFragment :
    BaseFragment<FragmentVotePromisePlaceBinding>(R.layout.fragment_vote_promise_place) {

    private val viewModel: VoteViewModel by viewModels()
    private lateinit var placeListAdapter: PlaceListAdapter

    private val args by navArgs<VotePromisePlaceFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meetId = args.meetId

        setupRecyclerView()
        observer()
        initView(meetId)
        addListeners()
    }

    private fun setupRecyclerView() {
        placeListAdapter = PlaceListAdapter().apply {
            setOnItemClickListener {

            }
        }
        binding.rvPlace.adapter = placeListAdapter
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timePlaceState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            placeListAdapter.submitList(it.data.placeItems)
                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun initView(meetId: Int) {
        viewModel.getTimePlaceCandidate(meetId)
    }


    private fun addListeners() {
        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}