package tk.svsq.newsfeed.presentation.common.decoration

import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpacingDecorationWithDivider(private val marginVertical: Int = 20) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = marginVertical
        outRect.top = marginVertical
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val styledAttributes: TypedArray = parent.context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        val divider = styledAttributes.getDrawable(0)
        styledAttributes.recycle()

        divider?.let { div ->
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom: Int = top + div.getIntrinsicHeight()
                div.setBounds(left, top, right, bottom)
                div.draw(c)
            }
        }
    }
}