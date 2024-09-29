package com.yomo.yakgwa.ui.invitation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.domain.model.response.VotePlaceResponseEntity.PlaceInfos
import com.yomo.yakgwa.databinding.ItemVotePlaceListBinding

class VotePlaceListAdapter :
    ListAdapter<PlaceInfos, VotePlaceListAdapter.VotePlaceListViewHolder>(
        VotePlaceDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotePlaceListViewHolder {
        return VotePlaceListViewHolder(
            ItemVotePlaceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VotePlaceListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class VotePlaceListViewHolder(private val binding: ItemVotePlaceListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: PlaceInfos) {
            binding.tvTitle.text = itemView.title
            binding.tvAddress.text = itemView.roadAddress
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val VotePlaceDiffCallback =
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