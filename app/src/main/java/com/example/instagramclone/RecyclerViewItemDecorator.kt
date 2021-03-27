package com.example.instagramclone

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class RecyclerViewItemDecorator(private val spaceInPixels: Int) : ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = spaceInPixels
        outRect.right = spaceInPixels
        outRect.bottom = spaceInPixels
        if (view?.let { parent.getChildLayoutPosition(it) } == 0) {
            outRect.top = spaceInPixels
        } else {
            outRect.top = 0
        }
    }

}