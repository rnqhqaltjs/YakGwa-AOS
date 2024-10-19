package com.yomo.yakgwa.ui.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yomo.domain.model.response.PlaceCandidateResponseEntity.UserInfos
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.ItemVotePlaceMemberListBinding

class VotePlaceMemberListAdapter :
    ListAdapter<UserInfos, VotePlaceMemberListAdapter.VotePlaceMemberListViewHolder>(
        PlaceDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VotePlaceMemberListViewHolder {
        return VotePlaceMemberListViewHolder(
            ItemVotePlaceMemberListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VotePlaceMemberListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class VotePlaceMemberListViewHolder(private val binding: ItemVotePlaceMemberListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: UserInfos) {
            binding.ivParticipantMember.load(itemView.imageUrl) {
                placeholder(R.drawable.ic_profile)
                error(R.drawable.ic_profile)
            }
        }
    }

    companion object {
        private val PlaceDiffCallback =
            object : DiffUtil.ItemCallback<UserInfos>() {
                override fun areItemsTheSame(
                    oldItem: UserInfos,
                    newItem: UserInfos
                ): Boolean {
                    return oldItem.username == newItem.username
                }

                override fun areContentsTheSame(
                    oldItem: UserInfos,
                    newItem: UserInfos
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}