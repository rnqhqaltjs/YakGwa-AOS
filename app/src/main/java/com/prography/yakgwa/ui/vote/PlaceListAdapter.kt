package com.prography.yakgwa.ui.vote

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.TimePlaceResponseEntity.PlaceItem
import com.prography.yakgwa.databinding.ItemPlaceListBinding

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetTextI18n")
class PlaceListAdapter :
    ListAdapter<PlaceItem, PlaceListAdapter.PlaceListViewHolder>(
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

        fun bind(itemView: PlaceItem) {

        }
    }

    private var onItemClickListener: ((PlaceItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (PlaceItem) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val PlaceDiffCallback =
            object : DiffUtil.ItemCallback<PlaceItem>() {
                override fun areItemsTheSame(
                    oldItem: PlaceItem,
                    newItem: PlaceItem
                ): Boolean {
                    return oldItem.candidatePlaceId == newItem.candidatePlaceId
                }

                override fun areContentsTheSame(
                    oldItem: PlaceItem,
                    newItem: PlaceItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}