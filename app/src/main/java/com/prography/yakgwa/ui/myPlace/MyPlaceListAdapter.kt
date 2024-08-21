package com.prography.yakgwa.ui.myPlace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.LocationResponseEntity
import com.prography.yakgwa.databinding.ItemMyPlaceListBinding

class MyPlaceListAdapter :
    ListAdapter<LocationResponseEntity, MyPlaceListAdapter.MyPlaceListViewHolder>(
        MyPlaceDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPlaceListViewHolder {
        return MyPlaceListViewHolder(
            ItemMyPlaceListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPlaceListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class MyPlaceListViewHolder(private val binding: ItemMyPlaceListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: LocationResponseEntity) {
            with(binding) {
                tvSelectedTitle.text = itemView.placeInfoEntity.title
                tvSelectedAddress.text = itemView.placeInfoEntity.roadAddress
                ivLikeBtn.isSelected = itemView.isUserLike
                ivLikeBtn.setOnClickListener {
                    onToggleClickListener?.invoke(itemView.placeInfoEntity, !itemView.isUserLike)
                }
            }
        }
    }

    private var onToggleClickListener: ((LocationResponseEntity.PlaceInfoEntity, Boolean) -> Unit)? =
        null

    fun setOnToggleClickListener(listener: (LocationResponseEntity.PlaceInfoEntity, Boolean) -> Unit) {
        onToggleClickListener = listener
    }

    companion object {
        private val MyPlaceDiffCallback =
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