package com.yomo.yakgwa.ui.createPromise.createPromisePlace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.domain.model.response.LocationResponseEntity
import com.yomo.yakgwa.databinding.ItemDirectLocationListBinding

class SelectedDirectLocationListAdapter :
    ListAdapter<LocationResponseEntity, SelectedDirectLocationListAdapter.SelectedLocationListViewHolder>(
        SelectedDirectLocationDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedLocationListViewHolder {
        return SelectedLocationListViewHolder(
            ItemDirectLocationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectedLocationListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class SelectedLocationListViewHolder(private val binding: ItemDirectLocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: LocationResponseEntity) {
            with(binding) {
                tvSelectedTitle.text = itemView.placeInfoEntity.title
                tvSelectedAddress.text = itemView.placeInfoEntity.roadAddress
                ivLike.isVisible = itemView.isUserLike
                ivRemoveBtn.setOnClickListener {
                    onRemoveClickListener?.invoke(itemView)
                }
            }
        }
    }

    private var onRemoveClickListener: ((LocationResponseEntity) -> Unit)? = null
    fun setOnRemoveClickListener(listener: (LocationResponseEntity) -> Unit) {
        onRemoveClickListener = listener
    }

    companion object {
        private val SelectedDirectLocationDiffCallback =
            object : DiffUtil.ItemCallback<LocationResponseEntity>() {
                override fun areItemsTheSame(
                    oldItem: LocationResponseEntity,
                    newItem: LocationResponseEntity
                ): Boolean {
                    return oldItem.placeInfoEntity.address == newItem.placeInfoEntity.address
                }

                override fun areContentsTheSame(
                    oldItem: LocationResponseEntity,
                    newItem: LocationResponseEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}