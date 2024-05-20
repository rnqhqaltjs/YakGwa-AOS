package com.prography.yakgwa.ui.createPromise

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentCreatePromiseBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePromiseFragment :
    BaseFragment<FragmentCreatePromiseBinding>(R.layout.fragment_create_promise) {

    private val viewModel: CreatePromiseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        addListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        lifecycleScope.launch {
            viewModel.textLengthState20.collectLatest { length ->
                binding.tvWithin20.text = "$length/20"
            }
        }

        lifecycleScope.launch {
            viewModel.textLengthState80.collectLatest { length ->
                binding.tvWithin80.text = "$length/80"
            }
        }

        binding.btnCreatePromise.setOnClickListener {
            CreatePromiseFragmentDirections.actionCreatePromiseFragmentToInvitationLeaderFragment()
                .apply {
                    findNavController().navigate(this)
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
    }

}