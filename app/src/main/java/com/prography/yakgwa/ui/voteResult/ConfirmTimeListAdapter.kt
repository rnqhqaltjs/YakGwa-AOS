package com.prography.yakgwa.ui.voteResult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.TimeCandidateResponseEntity.TimeInfo
import com.prography.yakgwa.databinding.ItemConfirmTimeListBinding
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanDate
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanTime

class ConfirmTimeListAdapter :
    ListAdapter<TimeInfo, ConfirmTimeListAdapter.ConfirmTimeListViewHolder>(
        ConfirmDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmTimeListViewHolder {
        return ConfirmTimeListViewHolder(
            ItemConfirmTimeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ConfirmTimeListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class ConfirmTimeListViewHolder(private val binding: ItemConfirmTimeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: TimeInfo) {
            with(binding) {
                radioButton.isChecked = itemView.isSelected
                tvDate.text = formatDateTimeToKoreanDate(itemView.voteTime)
                tvTime.text = formatDateTimeToKoreanTime(itemView.voteTime)
                cvConfirmTime.setOnClickListener {
                    onItemClickListener?.invoke(adapterPosition)
                }
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val ConfirmDiffCallback =
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