package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentCreatePromisePlaceBinding
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class CreatePromisePlaceFragment :
    BaseFragment<FragmentCreatePromisePlaceBinding>(R.layout.fragment_create_promise_place) {

    private val viewModel: CreatePromiseViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupJetpackNavigation()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createMeetState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            navigateToInvitationLeaderFragment(it.data.meetId)
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
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> navController.navigate(R.id.addCandidatePlaceFragment)
                    1 -> navController.navigate(R.id.directInputPlaceFragment)
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
            findNavController().navigateUp()
        }
    }

    private fun navigateToInvitationLeaderFragment(meetId: Int) {
        CreatePromisePlaceFragmentDirections.actionCreatePromisePlaceFragmentToInvitationLeaderFragment(
            meetId
        )
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