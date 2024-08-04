package com.prography.yakgwa.ui.invitation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.prography.domain.model.response.MeetDetailResponseEntity.ParticipantInfo
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.ItemParticipantMemberListBinding
import com.prography.yakgwa.type.RoleType

class ParticipantMemberListAdapter :
    ListAdapter<ParticipantInfo, ParticipantMemberListAdapter.ParticipantMemberListViewHolder>(
        ParticipantMemberDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParticipantMemberListViewHolder {
        return ParticipantMemberListViewHolder(
            ItemParticipantMemberListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ParticipantMemberListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class ParticipantMemberListViewHolder(private val binding: ItemParticipantMemberListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: ParticipantInfo) {
            binding.ivLeader.isVisible = itemView.meetRole == RoleType.LEADER.name
            binding.ivParticipantMember.load(itemView.imageUrl) {
                placeholder(R.drawable.ic_profile)
                error(R.drawable.ic_profile)
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val ParticipantMemberDiffCallback =
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