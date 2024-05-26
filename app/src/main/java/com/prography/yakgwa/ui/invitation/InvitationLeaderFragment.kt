package com.prography.yakgwa.ui.invitation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationLeaderBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvitationLeaderFragment :
    BaseFragment<FragmentInvitationLeaderBinding>(R.layout.fragment_invitation_leader) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()
    }

    private fun addListeners() {
        binding.btnInvitationMember.setOnClickListener {
            navigateToInvitationDetailLeaderFragment()
        }
    }

    private fun navigateToInvitationDetailLeaderFragment() {
        InvitationLeaderFragmentDirections.actionInvitationLeaderFragmentToInvitationDetailLeaderFragment()
            .apply {
                findNavController().navigate(this)
            }
    }
}