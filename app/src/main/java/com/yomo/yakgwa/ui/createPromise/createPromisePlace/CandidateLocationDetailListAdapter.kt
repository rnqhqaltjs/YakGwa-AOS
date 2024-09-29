package com.yomo.yakgwa.ui.createPromise.createPromisePlace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yomo.domain.model.response.LocationResponseEntity
import com.yomo.yakgwa.databinding.ItemCandidateDetailLocationListBinding

class CandidateLocationDetailListAdapter :
    ListAdapter<LocationResponseEntity, CandidateLocationDetailListAdapter.CandidateLocationDetailListViewHolder>(
        CandidateLocationDetailDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CandidateLocationDetailListViewHolder {
        return CandidateLocationDetailListViewHolder(
            ItemCandidateDetailLocationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CandidateLocationDetailListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class CandidateLocationDetailListViewHolder(private val binding: ItemCandidateDetailLocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: LocationResponseEntity) {
            with(binding) {
                tvSelectedTitle.text = itemView.placeInfoEntity.title
                tvSelectedAddress.text = itemView.placeInfoEntity.roadAddress
                cvSearchLocation.isSelected = itemView.isSelected
                ivLike.isVisible = itemView.isUserLike
                cvSearchLocation.setOnClickListener {
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
        private val CandidateLocationDetailDiffCallback =
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