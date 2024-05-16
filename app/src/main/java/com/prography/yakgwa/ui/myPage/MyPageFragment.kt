package com.prography.yakgwa.ui.myPage

import android.os.Bundle
import android.view.View
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentMyPageBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}