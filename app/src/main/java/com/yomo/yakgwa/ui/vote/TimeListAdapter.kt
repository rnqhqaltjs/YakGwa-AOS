package com.yomo.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.yakgwa.databinding.ItemTimeListBinding
import com.yomo.yakgwa.model.TimeModel

@SuppressLint("SetTextI18n")
class TimeListAdapter :
    ListAdapter<TimeModel, TimeListAdapter.TimeListViewHolder>(
        TimeDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeListViewHolder {
        return TimeListViewHolder(
            ItemTimeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class TimeListViewHolder(private val binding: ItemTimeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: TimeModel) {
            with(binding) {
                cvTimeSlot.isSelected = itemView.isSelected
                tvVoteCount.isSelected = itemView.isSelected
                tvHour.text = "${itemView.time.hour}시"
                tvVoteCount.text = itemView.voteCount.toString()
                cvTimeSlot.setOnClickListener {
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
        private val TimeDiffCallback =
            object : DiffUtil.ItemCallback<TimeModel>() {
                override fun areItemsTheSame(
                    oldItem: TimeModel, newItem: TimeModel
                ): Boolean {
                    return oldItem.time == newItem.time
                }

                override fun areContentsTheSame(
                    oldItem: TimeModel,
                    newItem: TimeModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}