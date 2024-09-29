package com.yomo.yakgwa.ui.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.domain.model.response.PlaceCandidateResponseEntity
import com.yomo.yakgwa.databinding.ItemPlaceListBinding

class PlaceListAdapter :
    ListAdapter<PlaceCandidateResponseEntity, PlaceListAdapter.PlaceListViewHolder>(
        PlaceDiffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListViewHolder {
        return PlaceListViewHolder(
            ItemPlaceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaceListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class PlaceListViewHolder(private val binding: ItemPlaceListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: PlaceCandidateResponseEntity) {
            with(binding) {
                tvTitle.text = itemView.placeName
                tvAddress.text = itemView.placeAddress
                cvPlace.isSelected = itemView.isSelected

                val votePlaceMemberListAdapter = VotePlaceMemberListAdapter()
                rvParticipantMember.adapter = votePlaceMemberListAdapter
                votePlaceMemberListAdapter.submitList(itemView.userInfos)

                cvPlace.setOnClickListener {
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
        private val PlaceDiffCallback =
            object : DiffUtil.ItemCallback<PlaceCandidateResponseEntity>() {
                override fun areItemsTheSame(
                    oldItem: PlaceCandidateResponseEntity,
                    newItem: PlaceCandidateResponseEntity
                ): Boolean {
                    return oldItem.placeSlotId == newItem.placeSlotId
                }

                override fun areContentsTheSame(
                    oldItem: PlaceCandidateResponseEntity,
                    newItem: PlaceCandidateResponseEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}