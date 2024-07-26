package com.prography.yakgwa.ui.voteResult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.yakgwa.databinding.ItemConfirmTimeListBinding
import com.prography.yakgwa.model.ConfirmTimeModel
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanDate
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanTime

class ConfirmTimeListAdapter :
    ListAdapter<ConfirmTimeModel, ConfirmTimeListAdapter.ConfirmTimeListViewHolder>(
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

        fun bind(itemView: ConfirmTimeModel) {
            binding.radioButton.isChecked = itemView.isSelected
            binding.tvDate.text = formatDateTimeToKoreanDate(itemView.timeInfo.voteTime)
            binding.tvTime.text = formatDateTimeToKoreanTime(itemView.timeInfo.voteTime)

            binding.cvConfirmTime.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val ConfirmDiffCallback =
            object : DiffUtil.ItemCallback<ConfirmTimeModel>() {
                override fun areItemsTheSame(
                    oldItem: ConfirmTimeModel,
                    newItem: ConfirmTimeModel
                ): Boolean {
                    return oldItem.timeInfo.timeId == newItem.timeInfo.timeId
                }

                override fun areContentsTheSame(
                    oldItem: ConfirmTimeModel,
                    newItem: ConfirmTimeModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}