package com.prography.yakgwa.ui.createPromise.createPromiseTime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentCreatePromiseTimeBinding
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel.Companion.TAB_ADD_CANDIDATE
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel.Companion.TAB_DIRECT_INPUT
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePromiseTimeFragment :
    BaseFragment<FragmentCreatePromiseTimeBinding>(R.layout.fragment_create_promise_time) {

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
        binding.tabLayout.getTabAt(viewModel.selectedTabTimeIndex.value)?.select()
    }

    private fun addListeners() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    TAB_ADD_CANDIDATE -> {
                        viewModel.selectedTabTimeIndex.value = TAB_ADD_CANDIDATE
                        navController.navigate(R.id.addCandidateTimeFragment)
                    }

                    TAB_DIRECT_INPUT -> {
                        viewModel.selectedTabTimeIndex.value = TAB_DIRECT_INPUT
                        navController.navigate(R.id.directInputTimeFragment)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.btnNext.setOnClickListener {
            navigateToInvitationCreatePromisePlaceFragment()
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
            viewModel.isTimeBtnEnabled.collectLatest { isEnabled ->
                binding.btnNext.isEnabled = isEnabled
            }
        }
    }

    private fun navigateToInvitationCreatePromisePlaceFragment() {
        CreatePromiseTimeFragmentDirections.actionCreatePromiseTimeFragmentToCreatePromisePlaceFragment()
            .apply {
                findNavController().navigate(this)
            }
    }

    private fun setupJetpackNavigation() {
        val host = getChildFragmentManager()
            .findFragmentById(R.id.nav_host_time_fragment) as NavHostFragment? ?: return
        navController = host.navController
    }
}