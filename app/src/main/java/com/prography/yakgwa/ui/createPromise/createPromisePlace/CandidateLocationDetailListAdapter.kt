package com.prography.yakgwa.ui.createPromise.createPromisePlace

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.yakgwa.databinding.ItemCandidateDetailLocationListBinding
import com.prography.yakgwa.model.SelectedLocationModel

class CandidateLocationDetailListAdapter :
    ListAdapter<SelectedLocationModel, CandidateLocationDetailListAdapter.CandidateLocationDetailListViewHolder>(
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
        fun bind(itemView: SelectedLocationModel) {
            binding.tvSelectedTitle.text =
                Html.fromHtml(itemView.locationResponseEntity.title).toString()
            binding.tvSelectedAddress.text = itemView.locationResponseEntity.roadAddress
            binding.cvSearchLocation.isSelected = itemView.isSelected

            binding.cvSearchLocation.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val CandidateLocationDetailDiffCallback =
            object : DiffUtil.ItemCallback<SelectedLocationModel>() {
                override fun areItemsTheSame(
                    oldItem: SelectedLocationModel,
                    newItem: SelectedLocationModel
                ): Boolean {
                    return oldItem.locationResponseEntity.roadAddress == newItem.locationResponseEntity.roadAddress
                }

                override fun areContentsTheSame(
                    oldItem: SelectedLocationModel,
                    newItem: SelectedLocationModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}