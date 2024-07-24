package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.yakgwa.databinding.ItemLocationListBinding

class DirectLocationListAdapter :
    ListAdapter<LocationResponseEntity, DirectLocationListAdapter.DirectLocationListViewHolder>(
        DirectLocationDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DirectLocationListViewHolder {
        return DirectLocationListViewHolder(
            ItemLocationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DirectLocationListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(itemView)
        }
    }

    inner class DirectLocationListViewHolder(private val binding: ItemLocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: LocationResponseEntity) {
            binding.tvTitle.text = Html.fromHtml(itemView.title).toString()
        }
    }

    private var onItemClickListener: ((LocationResponseEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (LocationResponseEntity) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val DirectLocationDiffCallback =
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