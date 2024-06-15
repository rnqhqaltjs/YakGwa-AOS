package com.prography.yakgwa.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentHomeBinding
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private var meetId: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.meetsState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            binding.tvVoteTimePlace.text =
                                it.data.reversed()[0].meetInfo.meetId.toString()
                            meetId = it.data.reversed()[0].meetInfo.meetId
                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun addListeners() {
        binding.btnGoCreatePromise.setOnClickListener {
            navigateToCreatePromiseFragment()
        }

        binding.tvVoteTimePlace.setOnClickListener {
            navigateToInvitationLeaderFragment()
        }
    }

    private fun navigateToCreatePromiseFragment() {
        HomeFragmentDirections.actionHomeFragmentToCreatePromiseFragment().apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToInvitationLeaderFragment() {
        HomeFragmentDirections.actionHomeFragmentToInvitationLeaderFragment(meetId!!).apply {
            findNavController().navigate(this)
        }
    }
}