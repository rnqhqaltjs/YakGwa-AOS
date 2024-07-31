package com.prography.yakgwa.ui.invitation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.DialogParticipantMemberModalBinding
import com.prography.yakgwa.util.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipantMemberFragment :
    BaseBottomSheetDialogFragment<DialogParticipantMemberModalBinding>(R.layout.dialog_participant_member_modal) {
    private val args by navArgs<ParticipantMemberFragmentArgs>()
    private lateinit var participantMemberEntireListAdapter: ParticipantMemberEntireListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        participantMemberEntireListAdapter.submitList(args.participantInfo.toList())
    }

    private fun setupRecyclerView() {
        participantMemberEntireListAdapter = ParticipantMemberEntireListAdapter()
        binding.rvMemberList.adapter = participantMemberEntireListAdapter
    }
}