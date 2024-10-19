package com.yomo.yakgwa.ui.voteResult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.domain.model.response.VotePlaceResponseEntity.PlaceInfos
import com.yomo.yakgwa.databinding.ItemConfirmPlaceListBinding

class ConfirmPlaceListAdapter :
    ListAdapter<PlaceInfos, ConfirmPlaceListAdapter.ConfirmPlaceListViewHolder>(
        ConfirmDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmPlaceListViewHolder {
        return ConfirmPlaceListViewHolder(
            ItemConfirmPlaceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ConfirmPlaceListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class ConfirmPlaceListViewHolder(private val binding: ItemConfirmPlaceListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: PlaceInfos) {
            with(binding) {
                radioButton.isChecked = itemView.isSelected
                tvPlace.text = itemView.title
                cvConfirmPlace.setOnClickListener {
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
            object : DiffUtil.ItemCallback<PlaceInfos>() {
                override fun areItemsTheSame(
                    oldItem: PlaceInfos,
                    newItem: PlaceInfos
                ): Boolean {
                    return oldItem.placeSlotId == newItem.placeSlotId
                }

                override fun areContentsTheSame(
                    oldItem: PlaceInfos,
                    newItem: PlaceInfos
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}