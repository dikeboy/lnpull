package com.lin.pull


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.lin.lnpull.R
/**
 * author : leo
 * date   : 2018/10/2812:07
 */
class PullLinearLayout : PullBaseLayout {
    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun getMainView(context: Context): View {
        // TODO Auto-generated method stub
        return LayoutInflater.from(context).inflate(
                R.layout.lnpull_base_loading, null)
    }


    fun addChildView(view: View?) {
        // TODO Auto-generated method stub
        if (view != null) {
            val param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            mainLayout!!.addView(view, param)
        }
    }
}
