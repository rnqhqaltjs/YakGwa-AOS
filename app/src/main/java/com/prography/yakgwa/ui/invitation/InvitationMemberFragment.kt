package com.prography.yakgwa.ui.invitation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationMemberBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvitationMemberFragment :
    BaseFragment<FragmentInvitationMemberBinding>(R.layout.fragment_invitation_member) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListeners()
    }

    private fun addListeners() {
        binding.btnInvitationParticipant.setOnClickListener {
            navigateToInvitationLeaderFragment()
        }
    }

    private fun navigateToInvitationLeaderFragment() {
        InvitationMemberFragmentDirections.actionInvitationMemberFragmentToInvitationLeaderFragment()
            .apply {
                findNavController().navigate(this)
            }
    }
}