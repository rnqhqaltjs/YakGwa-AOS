package com.prography.yakgwa.ui.invitation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.TimeCandidateResponseEntity.TimeInfo
import com.prography.yakgwa.databinding.ItemVoteTimeListBinding

class VoteTimeListAdapter :
    ListAdapter<TimeInfo, VoteTimeListAdapter.VoteTimeListViewHolder>(
        VoteTimeDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteTimeListViewHolder {
        return VoteTimeListViewHolder(
            ItemVoteTimeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VoteTimeListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class VoteTimeListViewHolder(private val binding: ItemVoteTimeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: TimeInfo) {
            binding.tvVoteDate.text = itemView.voteTime
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val VoteTimeDiffCallback =
            object : DiffUtil.ItemCallback<TimeInfo>() {
                override fun areItemsTheSame(
                    oldItem: TimeInfo,
                    newItem: TimeInfo
                ): Boolean {
                    return oldItem.timeId == newItem.timeId
                }

                override fun areContentsTheSame(
                    oldItem: TimeInfo,
                    newItem: TimeInfo
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}