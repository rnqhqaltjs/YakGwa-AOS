package com.prography.yakgwa.ui.createPromise

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentCreatePromiseTitleBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePromiseTitleFragment :
    BaseFragment<FragmentCreatePromiseTitleBinding>(R.layout.fragment_create_promise_title) {
    private val viewModel: CreatePromiseViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        addListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        lifecycleScope.launch {
            viewModel.textLength20State.collectLatest { text ->
                binding.tvWithin20.text = "${text.length}/20"
                binding.btnNext.isEnabled = text.isNotEmpty()
            }
        }

        lifecycleScope.launch {
            viewModel.textLength80State.collectLatest { text ->
                binding.tvWithin80.text = "${text.length}/80"
            }
        }
    }

    private fun addListeners() {
        binding.etWithin20Msg.addTextChangedListener { text ->
            viewModel.onTextChanged20(text.toString())
        }

        binding.etWithin80Msg.addTextChangedListener { text ->
            viewModel.onTextChanged80(text.toString())
        }

        binding.btnNext.setOnClickListener {
            navigateToCreatePromiseThemeFragment()
        }

        binding.navigateUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_exit_dialog)
        }

        binding.cvWithin80.setOnClickListener {
            binding.etWithin80Msg.requestFocus()
            binding.etWithin80Msg.performClick()
        }
    }

    private fun navigateToCreatePromiseThemeFragment() {
        CreatePromiseTitleFragmentDirections.actionCreatePromiseTitleFragmentToCreatePromiseThemeFragment()
            .apply {
                findNavController().navigate(this)
            }
    }
}