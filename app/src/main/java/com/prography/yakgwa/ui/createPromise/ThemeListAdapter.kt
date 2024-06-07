package com.prography.yakgwa.ui.createPromise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.yakgwa.databinding.ItemThemeListBinding
import com.prography.yakgwa.model.ThemeModel

class ThemeListAdapter :
    ListAdapter<ThemeModel, ThemeListAdapter.ThemeListViewHolder>(
        ThemeDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeListViewHolder {
        return ThemeListViewHolder(
            ItemThemeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ThemeListViewHolder, position: Int) {
        val itemView = currentList[position]
        holder.bind(itemView)
    }

    inner class ThemeListViewHolder(private val binding: ItemThemeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: ThemeModel) {
            binding.tvThemeName.text = itemView.themesResponseEntity.name
            binding.cvTheme.isSelected = itemView.isSelected

            binding.cvTheme.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val ThemeDiffCallback =
            object : DiffUtil.ItemCallback<ThemeModel>() {
                override fun areItemsTheSame(
                    oldItem: ThemeModel,
                    newItem: ThemeModel
                ): Boolean {
                    return oldItem.themesResponseEntity.meetThemeId == newItem.themesResponseEntity.meetThemeId
                }

                override fun areContentsTheSame(
                    oldItem: ThemeModel,
                    newItem: ThemeModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}