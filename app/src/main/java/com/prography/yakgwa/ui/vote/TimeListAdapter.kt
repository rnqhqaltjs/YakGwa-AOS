package com.prography.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.yakgwa.databinding.ItemTimeListBinding
import com.prography.yakgwa.model.DateTimeModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetTextI18n")
class TimeListAdapter :
    ListAdapter<DateTimeModel, TimeListAdapter.TimeListViewHolder>(
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

    private fun multiSelection(position: Int) {
        val items = currentList.toMutableList()

        val selectedItem = items[position]
        val updatedItem = selectedItem.copy(
            isSelected = !selectedItem.isSelected,
            voteCount = if (!selectedItem.isSelected) selectedItem.voteCount + 1 else selectedItem.voteCount - 1
        )

        items[position] = updatedItem

        submitList(items.toList())
    }

    inner class TimeListViewHolder(private val binding: ItemTimeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: DateTimeModel) {
            binding.tvHour.text = "${itemView.time.hour}시"

            binding.cvTimeSlot.isSelected = itemView.isSelected
            binding.tvVoteCount.isSelected = itemView.isSelected
            binding.tvVoteCount.text = itemView.voteCount.toString()

            binding.cvTimeSlot.setOnClickListener {
                onItemClickListener?.invoke(itemView)
            }
        }
    }

    private var onItemClickListener: ((DateTimeModel) -> Unit)? = null
    fun setOnItemClickListener(listener: (DateTimeModel) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val TimeDiffCallback =
            object : DiffUtil.ItemCallback<DateTimeModel>() {
                override fun areItemsTheSame(
                    oldItem: DateTimeModel,
                    newItem: DateTimeModel
                ): Boolean {
                    return oldItem.time == newItem.time
                }

                override fun areContentsTheSame(
                    oldItem: DateTimeModel,
                    newItem: DateTimeModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}