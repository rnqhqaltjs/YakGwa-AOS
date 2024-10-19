package com.yomo.yakgwa.ui.createPromise.createPromisePlace

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentCreatePromisePlaceBinding
import com.yomo.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.yomo.yakgwa.ui.createPromise.CreatePromiseViewModel.Companion.TAB_ADD_CANDIDATE
import com.yomo.yakgwa.ui.createPromise.CreatePromiseViewModel.Companion.TAB_DIRECT_INPUT
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import com.yomo.yakgwa.util.extensions.hide
import com.yomo.yakgwa.util.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePromisePlaceFragment :
    BaseFragment<FragmentCreatePromisePlaceBinding>(R.layout.fragment_create_promise_place) {

    private val viewModel: CreatePromiseViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupJetpackNavigation()
        observer()
        addListeners()
        initView()
    }

    private fun initView() {
        binding.tabLayout.getTabAt(viewModel.selectedTabPlaceIndex.value)?.select()
    }

    private fun addListeners() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    TAB_ADD_CANDIDATE -> {
                        viewModel.selectedTabPlaceIndex.value = TAB_ADD_CANDIDATE
                        navController.navigate(R.id.addCandidatePlaceFragment)
                    }

                    TAB_DIRECT_INPUT -> {
                        viewModel.selectedTabPlaceIndex.value = TAB_DIRECT_INPUT
                        navController.navigate(R.id.directInputPlaceFragment)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.btnCreatePromise.setOnClickListener {
            viewModel.createMeet()
        }

        binding.btnPrevious.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.navigateUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_exit_dialog)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createMeetState.collect {
                    when (it) {
                        is UiState.Loading -> {
                            binding.progressBar.show(requireActivity())
                        }

                        is UiState.Success -> {
                            binding.progressBar.hide(requireActivity())
                            handleCreateMeet(it.data.meetId)
                        }

                        is UiState.Failure -> {
                            binding.progressBar.hide(requireActivity())
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPlaceBtnEnabled.collectLatest { isEnabled ->
                    binding.btnCreatePromise.isEnabled = isEnabled
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isAddCandidateBtnClicked.collectLatest { isClicked ->
                if (isClicked) {
                    navigateToAddCandidatePlaceDetailFragment()
                    viewModel.onResetButtonClicked()
                }
            }
        }
    }

    private fun handleCreateMeet(meetId: Int) {
        if (viewModel.selectedTabPlaceIndex.value == TAB_DIRECT_INPUT &&
            viewModel.selectedTabTimeIndex.value == TAB_DIRECT_INPUT
        ) {
            navigateToVoteResultFragment(meetId)
        } else {
            navigateToInvitationLeaderFragment(meetId)
        }
    }

    private fun navigateToInvitationLeaderFragment(meetId: Int) {
        if (findNavController().currentDestination?.id == R.id.createPromisePlaceFragment) {
            CreatePromisePlaceFragmentDirections.actionCreatePromisePlaceFragmentToInvitationLeaderFragment(
                meetId
            ).apply {
                findNavController().navigate(this)
            }
        }
    }

    private fun navigateToVoteResultFragment(meetId: Int) {
        if (findNavController().currentDestination?.id == R.id.createPromisePlaceFragment) {
            CreatePromisePlaceFragmentDirections.actionCreatePromisePlaceFragmentToVoteResultFragment(
                meetId
            ).apply {
                findNavController().navigate(this)
            }
        }
    }

    private fun navigateToAddCandidatePlaceDetailFragment() {
        CreatePromisePlaceFragmentDirections.actionCreatePromisePlaceFragmentToAddCandidatePlaceDetailFragment()
            .apply {
                findNavController().navigate(this)
            }
    }

    private fun setupJetpackNavigation() {
        val host = getChildFragmentManager()
            .findFragmentById(R.id.nav_host_place_fragment) as NavHostFragment? ?: return
        navController = host.navController
    }
}