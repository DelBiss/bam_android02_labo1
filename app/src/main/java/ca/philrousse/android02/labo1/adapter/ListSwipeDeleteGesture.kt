package ca.philrousse.android02.labo1.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class ListSwipeDeleteGesture(
        private val icon:Drawable,
        private val color:Int = Color.RED,
        private val iconMargin:Int = 8,
        private val onSwipeCallback:(position:Int)->Unit
    ): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeCallback(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        c.clipRect(0f, viewHolder.itemView.top.toFloat(), dX, viewHolder.itemView.bottom.toFloat())
        val progressionColor = interpolateColor(calculateProgression(dX, dY, viewHolder))
        c.drawColor(progressionColor)
        icon.bounds = getIconBounds(viewHolder.itemView)
        icon.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun getIconBounds(itemView:View): Rect {
        val iconSize = itemView.height - (iconMargin * 2)
        return Rect(
            iconMargin,
            itemView.top + iconMargin,
            iconMargin + iconSize,
            itemView.top + iconMargin + iconSize
        )
    }
    private fun calculateProgression(dX:Float,dY:Float,viewHolder: RecyclerView.ViewHolder): Float {
        return min(dX / (viewHolder.itemView.width / 2), 1F)
    }

    private fun interpolateColor(pct:Float):Int{
        val myHSLColor = FloatArray(3)
        Color.colorToHSV(color, myHSLColor)
        val cx = 0.6F
        myHSLColor[1] = (pct * cx) + (1 - cx)
        myHSLColor[2] = (pct * cx) + (1 - cx)
        return Color.HSVToColor(myHSLColor)
    }

}