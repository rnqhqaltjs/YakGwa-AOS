package com.yomo.yakgwa.ui.promiseHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.domain.model.response.PromiseHistoryResponseEntity
import com.yomo.yakgwa.databinding.ItemPromiseHistoryListBinding
import com.yomo.yakgwa.util.DateTimeUtils.formatIsoDateTimeToKoreanDate
import com.yomo.yakgwa.util.DateTimeUtils.formatIsoDateTimeToKoreanTime

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
            with(binding) {
                tvTitle.text = itemView.meetInfo.meetTitle
                tvTemaName.text = itemView.meetInfo.meetThemeName
                tvDescription.text = itemView.description
                itemView.meetInfo.meetDateTime?.let { dateTime ->
                    tvDate.text = formatIsoDateTimeToKoreanDate(dateTime)
                    tvTime.text = formatIsoDateTimeToKoreanTime(dateTime)
                }
                tvPlace.text = itemView.meetInfo.placeName
                tvShowDetail.setOnClickListener {
                    onItemClickListener?.invoke(itemView.meetInfo.meetId)
                }
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