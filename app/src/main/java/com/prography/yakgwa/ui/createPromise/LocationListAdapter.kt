package com.prography.yakgwa.ui.createPromise

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.yakgwa.databinding.ItemLocationListBinding

class LocationListAdapter :
    ListAdapter<LocationResponseEntity, LocationListAdapter.LocationListViewHolder>(
        LocationDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationListViewHolder {
        return LocationListViewHolder(
            ItemLocationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocationListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(itemView)
        }
    }

    inner class LocationListViewHolder(private val binding: ItemLocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: LocationResponseEntity) {
            binding.tvTitle.text = Html.fromHtml(itemView.title)
        }
    }

    private var onItemClickListener: ((LocationResponseEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (LocationResponseEntity) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val LocationDiffCallback =
            object : DiffUtil.ItemCallback<LocationResponseEntity>() {
                override fun areItemsTheSame(
                    oldItem: LocationResponseEntity,
                    newItem: LocationResponseEntity
                ): Boolean {
                    return oldItem.title == newItem.title
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