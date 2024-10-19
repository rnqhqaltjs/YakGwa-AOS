package com.yomo.yakgwa.ui.invitation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yomo.domain.model.response.MeetDetailResponseEntity.ParticipantInfo
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.ItemParticipantMemberEntireListBinding
import com.yomo.yakgwa.type.RoleType

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
            with(binding) {
                tvMemberName.text = itemView.name
                ivLeader.isVisible = itemView.meetRole == RoleType.LEADER.name
                ivParticipantMember.load(itemView.imageUrl) {
                    placeholder(R.drawable.ic_profile)
                    error(R.drawable.ic_profile)
                }
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
                    return oldItem.name == newItem.name
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