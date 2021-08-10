package cn.entergx.yztx.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView

class NestedFuckView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    private val TAG = "123123"

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: "+ev?.toString())
        return super.onTouchEvent(ev)
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.d(TAG, "onNestedFling: $velocityX:$velocityY")
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }


    override fun arrowScroll(direction: Int): Boolean {
        return super.arrowScroll(direction)
    }

    @SuppressLint("RestrictedApi")
    override fun computeVerticalScrollOffset(): Int {
        Log.d(TAG, "computeVerticalScrollOffset: "+super.computeVerticalScrollOffset())
        return super.computeVerticalScrollOffset()
    }
}