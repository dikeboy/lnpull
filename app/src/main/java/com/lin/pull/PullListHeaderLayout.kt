package com.lin.pull

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * author : leo
 * date   : 2018/10/2812:07
 */
class PullListHeaderLayout : ViewGroup {
    var mHeight: Int = 0
    protected val childView: View?
        get() {
           return  if(childCount ==1) getChildAt(0) else null
        }

    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        if (mHeight < 0) {
            mHeight = 0
        }
        setMeasuredDimension(width, mHeight)
        val childView = childView
        childView?.measure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // TODO Auto-generated method stub
        val childView = childView ?: return

        val childViewWidth = childView.measuredWidth
        val childViewHeight = childView.measuredHeight
        val measuredHeight = measuredHeight
        childView.layout(0, measuredHeight - childViewHeight, childViewWidth,
                measuredHeight)
    }

    fun setHeaderHeight(height: Int) {
        if (mHeight == height && height == 0) {
            // ignore duplicate 0 height setting.
            return
        }
        this.mHeight = height
        requestLayout()
    }

}
