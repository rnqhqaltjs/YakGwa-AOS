package com.prography.yakgwa.ui.createPromise

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.yakgwa.databinding.ItemSelectedLocationListBinding
import com.prography.yakgwa.model.SelectedLocationModel

class SelectedLocationListAdapter :
    ListAdapter<SelectedLocationModel, SelectedLocationListAdapter.SelectedLocationListViewHolder>(
        SelectedLocationDiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedLocationListViewHolder {
        return SelectedLocationListViewHolder(
            ItemSelectedLocationListBinding.inflate(
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

    inner class SelectedLocationListViewHolder(private val binding: ItemSelectedLocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: SelectedLocationModel) {
            binding.tvSelectedTitle.text = Html.fromHtml(itemView.title)

            binding.ivRemoveBtn.setOnClickListener {
                onRemoveClickListener?.invoke(itemView)
            }
        }
    }

    private var onRemoveClickListener: ((SelectedLocationModel) -> Unit)? = null
    fun setOnRemoveClickListener(listener: (SelectedLocationModel) -> Unit) {
        onRemoveClickListener = listener
    }

    companion object {
        private val SelectedLocationDiffCallback =
            object : DiffUtil.ItemCallback<SelectedLocationModel>() {
                override fun areItemsTheSame(
                    oldItem: SelectedLocationModel,
                    newItem: SelectedLocationModel
                ): Boolean {
                    return oldItem.title == newItem.title
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