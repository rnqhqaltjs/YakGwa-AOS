package com.yomo.yakgwa.ui.promiseHistory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentPromiseHistoryBinding
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromiseHistoryFragment :
    BaseFragment<FragmentPromiseHistoryBinding>(R.layout.fragment_promise_history) {
    private val viewModel: PromiseHistoryViewModel by viewModels()
    private lateinit var promiseHistoryListAdapter: PromiseHistoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.promiseHistoryState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            promiseHistoryListAdapter.submitList(
                                it.data.sortedByDescending { promise ->
                                    promise.meetInfo.meetDateTime
                                }
                            )
                            updateNoPromiseHistoryVisibility()
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
        promiseHistoryListAdapter = PromiseHistoryListAdapter().apply {
            setOnItemClickListener {
                navigateToVoteResultFragment(it)
            }
        }
        binding.rvPromiseHistory.adapter = promiseHistoryListAdapter
    }


    private fun addListeners() {
        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivGoCreatePromiseBtn.setOnClickListener {
            navigateToCreatePromiseTitleFragment()
        }
    }

    private fun updateNoPromiseHistoryVisibility() {
        if (promiseHistoryListAdapter.itemCount == 0) {
            binding.goMakePromise.visibility = View.VISIBLE
        } else {
            binding.goMakePromise.visibility = View.INVISIBLE
        }
    }

    private fun navigateToCreatePromiseTitleFragment() {
        PromiseHistoryFragmentDirections.actionPromiseHistoryFragmentToCreatePromiseTitleFragment()
            .apply {
                findNavController().navigate(this)
            }
    }

    private fun navigateToVoteResultFragment(meetId: Int) {
        PromiseHistoryFragmentDirections.actionPromiseHistoryFragmentToVoteResultFragment(meetId)
            .apply {
                findNavController().navigate(this)
            }
    }
}