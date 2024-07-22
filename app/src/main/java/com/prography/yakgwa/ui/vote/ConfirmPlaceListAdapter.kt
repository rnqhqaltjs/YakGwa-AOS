package com.prography.yakgwa.ui.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.yakgwa.databinding.ItemConfirmPlaceListBinding
import com.prography.yakgwa.model.ConfirmPlaceModel

class ConfirmPlaceListAdapter :
    ListAdapter<ConfirmPlaceModel, ConfirmPlaceListAdapter.ConfirmPlaceListViewHolder>(
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

        fun bind(itemView: ConfirmPlaceModel) {
            binding.radioButton.isChecked = itemView.isSelected
            binding.tvPlace.text = itemView.placeInfos.title

            binding.cvConfirmPlace.setOnClickListener {
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
            object : DiffUtil.ItemCallback<ConfirmPlaceModel>() {
                override fun areItemsTheSame(
                    oldItem: ConfirmPlaceModel,
                    newItem: ConfirmPlaceModel
                ): Boolean {
                    return oldItem.placeInfos.placeSlotId == newItem.placeInfos.placeSlotId
                }

                override fun areContentsTheSame(
                    oldItem: ConfirmPlaceModel,
                    newItem: ConfirmPlaceModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}