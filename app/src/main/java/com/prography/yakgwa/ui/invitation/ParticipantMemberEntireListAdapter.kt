package com.prography.yakgwa.ui.invitation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.prography.domain.model.response.MeetDetailResponseEntity.ParticipantInfo
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.ItemParticipantMemberEntireListBinding
import com.prography.yakgwa.type.RoleType

class ParticipantMemberEntireListAdapter :
    ListAdapter<ParticipantInfo, ParticipantMemberEntireListAdapter.ParticipantMemberEntireListViewHolder>(
        ParticipantMemberEntireDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParticipantMemberEntireListViewHolder {
        return ParticipantMemberEntireListViewHolder(
            ItemParticipantMemberEntireListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ParticipantMemberEntireListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class ParticipantMemberEntireListViewHolder(private val binding: ItemParticipantMemberEntireListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: ParticipantInfo) {
            binding.ivParticipantMember.load(itemView.imageUrl) {
                placeholder(R.drawable.ic_profile)
                error(R.drawable.ic_profile)
            }

            if (itemView.meetRole == RoleType.LEADER.name) {
                binding.ivLeader.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private val ParticipantMemberEntireDiffCallback =
            object : DiffUtil.ItemCallback<ParticipantInfo>() {
                override fun areItemsTheSame(
                    oldItem: ParticipantInfo,
                    newItem: ParticipantInfo
                ): Boolean {
                    return oldItem.participantId == newItem.participantId
                }

                override fun areContentsTheSame(
                    oldItem: ParticipantInfo,
                    newItem: ParticipantInfo
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}