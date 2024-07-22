package com.prography.yakgwa.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OverlapDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val overlapPx: Int = context.dpToPx(12f)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != 0) {
            outRect.right = -overlapPx
        }
    }

    private fun Context.dpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
