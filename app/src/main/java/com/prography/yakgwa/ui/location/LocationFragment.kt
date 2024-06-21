package com.prography.yakgwa.ui.location

import android.os.Bundle
import android.view.View
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentLocationBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}