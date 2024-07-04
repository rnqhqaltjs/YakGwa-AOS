package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentAddCandidatePlaceBinding
import com.prography.yakgwa.ui.createPromise.CreatePromiseViewModel
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class AddCandidatePlaceFragment :
    BaseFragment<FragmentAddCandidatePlaceBinding>(R.layout.fragment_add_candidate_place) {
    private val viewModel: CreatePromiseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()
    }

    private fun addListeners() {
        binding.etSearchLocation.addTextChangedListener { text ->
            viewModel.setSearchQuery(text.toString())
        }
    }

}