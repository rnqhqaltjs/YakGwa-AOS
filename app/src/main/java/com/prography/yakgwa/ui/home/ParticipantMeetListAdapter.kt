package com.prography.yakgwa.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.MeetsResponseEntity
import com.prography.yakgwa.databinding.ItemBeforeConfirmBinding
import com.prography.yakgwa.databinding.ItemBeforeVoteBinding
import com.prography.yakgwa.databinding.ItemConfirmBinding
import com.prography.yakgwa.databinding.ItemVoteBinding
import com.prography.yakgwa.type.MeetType

class ParticipantMeetListAdapter :
    ListAdapter<MeetsResponseEntity, RecyclerView.ViewHolder>(
        ParticipantMeetDiffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MeetType.BEFORE_VOTE.ordinal -> BeforeVoteViewHolder(
                ItemBeforeVoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            MeetType.VOTE.ordinal -> VoteViewHolder(
                ItemVoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            MeetType.BEFORE_CONFIRM.ordinal -> BeforeConfirmViewHolder(
                ItemBeforeConfirmBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            MeetType.CONFIRM.ordinal -> ConfirmViewHolder(
                ItemConfirmBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = currentList[position]
        when (holder) {
            is ConfirmViewHolder -> holder.bind(itemView)
            is BeforeConfirmViewHolder -> holder.bind(itemView)
            is VoteViewHolder -> holder.bind(itemView)
            is BeforeVoteViewHolder -> holder.bind(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].meetStatus) {
            MeetType.CONFIRM.name -> MeetType.CONFIRM.ordinal
            MeetType.BEFORE_CONFIRM.name -> MeetType.BEFORE_CONFIRM.ordinal
            MeetType.VOTE.name -> MeetType.VOTE.ordinal
            MeetType.BEFORE_VOTE.name -> MeetType.BEFORE_VOTE.ordinal
            else -> throw IllegalArgumentException("Invalid meet status: ${currentList[position].meetStatus}")
        }
    }

    inner class BeforeVoteViewHolder(private val binding: ItemBeforeVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: MeetsResponseEntity) {
            binding.tvInvitationTitle.text = itemView.meetInfo.meetTitle
            binding.tvTemaName.text = itemView.meetInfo.meetThemeName

            binding.btnTimePlaceVote.setOnClickListener {
                onItemClickListener?.invoke(itemView)
            }
        }
    }

    inner class VoteViewHolder(private val binding: ItemVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: MeetsResponseEntity) {
            binding.tvInvitationTitle.text = itemView.meetInfo.meetTitle
            binding.tvTemaName.text = itemView.meetInfo.meetThemeName

            binding.btnMeetDetail.setOnClickListener {
                onItemClickListener?.invoke(itemView)
            }
        }
    }

    inner class BeforeConfirmViewHolder(private val binding: ItemBeforeConfirmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: MeetsResponseEntity) {
            binding.tvInvitationTitle.text = itemView.meetInfo.meetTitle
            binding.tvTemaName.text = itemView.meetInfo.meetThemeName

            binding.btnMeetDetail.setOnClickListener {
                onItemClickListener?.invoke(itemView)
            }
        }
    }

    inner class ConfirmViewHolder(private val binding: ItemConfirmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: MeetsResponseEntity) {
            binding.tvInvitationTitle.text = itemView.meetInfo.meetTitle
            binding.tvTemaName.text = itemView.meetInfo.meetThemeName
//            binding.tvDDay.text = itemView.meetInfo.meetDateTime

            binding.tvPlace.text = itemView.meetInfo.placeName

            binding.btnMeetDetail.setOnClickListener {
                onItemClickListener?.invoke(itemView)
            }
        }
    }

    private var onItemClickListener: ((MeetsResponseEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (MeetsResponseEntity) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val ParticipantMeetDiffCallback =
            object : DiffUtil.ItemCallback<MeetsResponseEntity>() {
                override fun areItemsTheSame(
                    oldItem: MeetsResponseEntity,
                    newItem: MeetsResponseEntity
                ): Boolean {
                    return oldItem.meetInfo.meetId == newItem.meetInfo.meetId
                }

                override fun areContentsTheSame(
                    oldItem: MeetsResponseEntity,
                    newItem: MeetsResponseEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}