package com.prography.yakgwa.ui.promiseHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.PromiseHistoryResponseEntity
import com.prography.yakgwa.databinding.ItemPromiseHistoryListBinding
import com.prography.yakgwa.util.DateTimeUtils.formatIsoDateTimeToKoreanDate
import com.prography.yakgwa.util.DateTimeUtils.formatIsoDateTimeToKoreanTime

class PromiseHistoryListAdapter :
    ListAdapter<PromiseHistoryResponseEntity, PromiseHistoryListAdapter.PromiseHistoryListViewHolder>(
        PromiseHistoryDiffCallback
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromiseHistoryListViewHolder {
        return PromiseHistoryListViewHolder(
            ItemPromiseHistoryListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PromiseHistoryListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class PromiseHistoryListViewHolder(private val binding: ItemPromiseHistoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: PromiseHistoryResponseEntity) {
            binding.tvTitle.text = itemView.meetInfo.meetTitle
            binding.tvTemaName.text = itemView.meetInfo.meetThemeName
            binding.tvDescription.text = itemView.description
            itemView.meetInfo.meetDateTime?.let { dateTime ->
                binding.tvDate.text = formatIsoDateTimeToKoreanDate(dateTime)
                binding.tvTime.text = formatIsoDateTimeToKoreanTime(dateTime)
            }
            binding.tvPlace.text = itemView.meetInfo.placeName
            binding.tvShowDetail.setOnClickListener {
                onItemClickListener?.invoke(itemView.meetInfo.meetId)
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val PromiseHistoryDiffCallback =
            object : DiffUtil.ItemCallback<PromiseHistoryResponseEntity>() {
                override fun areItemsTheSame(
                    oldItem: PromiseHistoryResponseEntity,
                    newItem: PromiseHistoryResponseEntity
                ): Boolean {
                    return oldItem.meetInfo.meetId == newItem.meetInfo.meetId
                }

                override fun areContentsTheSame(
                    oldItem: PromiseHistoryResponseEntity,
                    newItem: PromiseHistoryResponseEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}