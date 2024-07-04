package com.prography.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.yakgwa.databinding.ItemPlaceListBinding
import com.prography.yakgwa.model.PlaceModel

@SuppressLint("SetTextI18n")
class PlaceListAdapter :
    ListAdapter<PlaceModel, PlaceListAdapter.PlaceListViewHolder>(
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

        fun bind(itemView: PlaceModel) {
            binding.tvTitle.text = itemView.placeItem.name
            binding.tvDescription.text = itemView.placeItem.description
            binding.tvAddress.text = itemView.placeItem.address

            binding.cvPlace.isSelected = itemView.isSelected

            binding.cvPlace.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val PlaceDiffCallback =
            object : DiffUtil.ItemCallback<PlaceModel>() {
                override fun areItemsTheSame(
                    oldItem: PlaceModel,
                    newItem: PlaceModel
                ): Boolean {
                    return oldItem.placeItem.candidatePlaceId == newItem.placeItem.candidatePlaceId
                }

                override fun areContentsTheSame(
                    oldItem: PlaceModel,
                    newItem: PlaceModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}