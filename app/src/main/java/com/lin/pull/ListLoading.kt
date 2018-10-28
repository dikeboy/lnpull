package com.lin.pull

import android.view.View
import android.widget.LinearLayout.LayoutParams
/**
 * author : leo
 * date   : 2018/10/2812:07
 */
interface ListLoading {
    fun startLoading()
    fun finishLoading()
    fun finishLoadingNoAnimation()
    fun showErrorView()
    fun showEmptyView()
    fun addBottomView(view: View)
    fun addBottomView(view: View, params: LayoutParams)
}
