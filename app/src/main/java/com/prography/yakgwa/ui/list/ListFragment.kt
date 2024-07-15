package com.prography.yakgwa.ui.list

import android.os.Bundle
import android.view.View
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentListBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>(R.layout.fragment_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}