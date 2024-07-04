package com.prography.yakgwa.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.prography.domain.model.response.MeetsResponseEntity
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
                            if (it.data.isNotEmpty()) {
                                updateUI(it.data.reversed()[0])
                            }

                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(meet: MeetsResponseEntity) {
        meetId = meet.meetInfo.meetId
        binding.tvInvitationTitle.text = meetId.toString()

        when (meet.meetStatus) {
            "VOTE" -> {
                binding.cvNoPromise.visibility = View.GONE
                binding.cvVote.visibility = View.VISIBLE
            }

            "BEFORE_CONFIRM" -> {
                if (meet.meetInfo.userVote) {
                    binding.cvNoPromise.visibility = View.GONE
                    binding.cvVote.visibility = View.VISIBLE
                    binding.beforeVote.visibility = View.GONE
                    binding.waitVote.visibility = View.VISIBLE
                } else {
                    binding.cvNoPromise.visibility = View.GONE
                    binding.cvVote.visibility = View.VISIBLE
                    binding.beforeVote.visibility = View.VISIBLE
                    binding.waitVote.visibility = View.GONE
                }
            }

            "CONFIRM" -> {
                binding.cvNoPromise.visibility = View.GONE
                binding.cvVote.visibility = View.VISIBLE
                binding.beforeVote.visibility = View.GONE
                binding.waitVote.visibility = View.VISIBLE
            }
        }
    }

    private fun addListeners() {
        binding.btnGoCreatePromise.setOnClickListener {
            navigateToCreatePromiseTitleFragment()
        }

        binding.btnTimePlaceVote.setOnClickListener {
            navigateToInvitationLeaderFragment()
        }

        binding.ivAddBtn.setOnClickListener {
            navigateToCreatePromiseTitleFragment()
        }

        binding.btnMeetInfoDetail.setOnClickListener {
            navigateToVoteCompletionFragment()
        }
    }

    private fun navigateToCreatePromiseTitleFragment() {
        HomeFragmentDirections.actionHomeFragmentToCreatePromiseTitleFragment().apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToInvitationLeaderFragment() {
        HomeFragmentDirections.actionHomeFragmentToInvitationLeaderFragment(meetId!!).apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToVoteCompletionFragment() {
        HomeFragmentDirections.actionHomeFragmentToVoteCompletionFragment(meetId!!).apply {
            findNavController().navigate(this)
        }
    }
}