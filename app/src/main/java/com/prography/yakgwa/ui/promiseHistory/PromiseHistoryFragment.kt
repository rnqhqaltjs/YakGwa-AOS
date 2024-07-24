package com.prography.yakgwa.ui.promiseHistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentPromiseHistoryBinding
import com.prography.yakgwa.ui.myPage.MyPageViewModel
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseHistoryFragment :
    BaseFragment<FragmentPromiseHistoryBinding>(R.layout.fragment_promise_history) {
    private val viewModel: MyPageViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        addListeners()
    }

    private fun observer() {
    }

    private fun addListeners() {
        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivGoCreatePromiseBtn.setOnClickListener {
            navigateToCreatePromiseTitleFragment()
        }
    }

    private fun navigateToCreatePromiseTitleFragment() {
        PromiseHistoryFragmentDirections.actionPromiseHistoryFragmentToCreatePromiseTitleFragment()
            .apply {
                findNavController().navigate(this)
            }
    }
}