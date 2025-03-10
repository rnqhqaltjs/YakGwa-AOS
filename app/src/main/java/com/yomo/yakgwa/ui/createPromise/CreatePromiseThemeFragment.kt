package com.yomo.yakgwa.ui.createPromise

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentCreatePromiseThemeBinding
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePromiseThemeFragment :
    BaseFragment<FragmentCreatePromiseThemeBinding>(R.layout.fragment_create_promise_theme) {
    private val viewModel: CreatePromiseViewModel by activityViewModels()

    private lateinit var themeListAdapter: ThemeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedThemeState.collectLatest { selectedTheme ->
                themeListAdapter.submitList(selectedTheme)
                binding.btnNext.isEnabled = !selectedTheme.none { it.isSelected }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.themesState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            if (viewModel.selectedThemeState.value.isEmpty()) {
                                themeListAdapter.submitList(it.data)
                            }
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
        themeListAdapter = ThemeListAdapter().apply {
            setOnItemClickListener { position ->
                viewModel.singleThemeSelection(position)
            }
        }
        binding.rvTema.adapter = themeListAdapter
    }

    private fun addListeners() {
        binding.btnNext.setOnClickListener {
            navigateToCreatePromiseTimeFragment()
        }

        binding.btnPrevious.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.navigateUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_exit_dialog)
        }
    }

    private fun navigateToCreatePromiseTimeFragment() {
        CreatePromiseThemeFragmentDirections.actionCreatePromiseThemeFragmentToCreatePromiseTimeFragment()
            .apply {
                findNavController().navigate(this)
            }
    }
}