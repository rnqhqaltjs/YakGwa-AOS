package com.yomo.yakgwa.ui.invitation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.domain.model.response.TimeCandidateResponseEntity.TimeInfo
import com.yomo.yakgwa.databinding.ItemVoteTimeListBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        if (isRangeStart(position)) {
            holder.itemView.visibility = View.VISIBLE
        } else {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }

    inner class VoteTimeListViewHolder(private val binding: ItemVoteTimeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: TimeInfo) {
            val (dateText, timeText) = formatTimeRange(itemView.voteTime, adapterPosition)
            binding.tvVoteDate.text = dateText
            binding.tvVoteTime.text = timeText
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    private fun isRangeStart(position: Int): Boolean {
        val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentStartTime = LocalDateTime.parse(getItem(position).voteTime, timeFormat)

        for (i in position - 1 downTo 0) {
            val previousItem = getItem(i)
            val previousStartTime = LocalDateTime.parse(previousItem.voteTime, timeFormat)
            if (currentStartTime.minusHours((position - i).toLong()).isEqual(previousStartTime)) {
                return false
            }
        }

        return true
    }

    private fun formatTimeRange(voteTime: String, position: Int): Pair<String, String> {
        val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateFormat = DateTimeFormatter.ofPattern("M/d")
        val hourFormat = DateTimeFormatter.ofPattern("H시")

        val currentStartTime = LocalDateTime.parse(voteTime, timeFormat)
        var endPosition = position

        for (i in position + 1 until currentList.size) {
            val nextItem = getItem(i)
            val nextStartTime = LocalDateTime.parse(nextItem.voteTime, timeFormat)
            if (currentStartTime.plusHours((i - position).toLong()).isEqual(nextStartTime)) {
                endPosition = i
            } else {
                break
            }
        }

        val startTimeText = hourFormat.format(currentStartTime)
        val dateText = dateFormat.format(currentStartTime)
        val endTimeText = if (endPosition > position) {
            val endTime = LocalDateTime.parse(getItem(endPosition).voteTime, timeFormat)
            "$startTimeText - ${hourFormat.format(endTime)}"
        } else {
            startTimeText
        }

        return Pair(dateText, endTimeText)
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