package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
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
            binding.tvSelectedTitle.text = Html.fromHtml(itemView.title).toString()
            binding.tvSelectedAddress.text = itemView.roadAddress

            binding.ivRemoveBtn.setOnClickListener {
                onRemoveClickListener?.invoke(itemView)
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
                    return oldItem.roadAddress == newItem.roadAddress
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