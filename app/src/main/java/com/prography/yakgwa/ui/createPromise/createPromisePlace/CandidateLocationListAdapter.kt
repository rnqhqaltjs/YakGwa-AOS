package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.yakgwa.databinding.ItemCandidateLocationListBinding

class CandidateLocationListAdapter :
    ListAdapter<LocationResponseEntity, CandidateLocationListAdapter.CandidateLocationListViewHolder>(
        CandidateLocationDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CandidateLocationListViewHolder {
        return CandidateLocationListViewHolder(
            ItemCandidateLocationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CandidateLocationListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class CandidateLocationListViewHolder(private val binding: ItemCandidateLocationListBinding) :
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
        private val CandidateLocationDiffCallback =
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