package com.prography.yakgwa.ui.createPromise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.domain.model.response.ThemesResponseEntity
import com.prography.yakgwa.databinding.ItemThemeListBinding

class ThemeListAdapter :
    ListAdapter<ThemesResponseEntity, ThemeListAdapter.ThemeListViewHolder>(
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
        fun bind(itemView: ThemesResponseEntity) {
            with(binding) {
                tvThemeName.text = itemView.themeName
                cvTheme.isSelected = itemView.isSelected
                cvTheme.setOnClickListener {
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
        private val ThemeDiffCallback =
            object : DiffUtil.ItemCallback<ThemesResponseEntity>() {
                override fun areItemsTheSame(
                    oldItem: ThemesResponseEntity,
                    newItem: ThemesResponseEntity
                ): Boolean {
                    return oldItem.themeId == newItem.themeId
                }

                override fun areContentsTheSame(
                    oldItem: ThemesResponseEntity,
                    newItem: ThemesResponseEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}