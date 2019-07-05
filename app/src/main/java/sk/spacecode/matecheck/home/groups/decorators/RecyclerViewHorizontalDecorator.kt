package sk.spacecode.matecheck.home.groups.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewHorizontalDecorator(private val spaceHeight: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {

        super.getItemOffsets(outRect, view, parent, state)

        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = spaceHeight
            }
            top = spaceHeight
            right = spaceHeight
            bottom = spaceHeight
        }
    }
}