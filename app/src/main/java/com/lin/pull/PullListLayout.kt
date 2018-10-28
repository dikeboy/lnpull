package com.lin.pull

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.RelativeLayout
import android.widget.TextView
import com.lin.lnpull.R
import com.lin.pull.PullToRefreshListView.OnRefreshListener

/**
 * author : leo
 * date   : 2018/10/2812:07
 */
@SuppressLint("NewApi")
class PullListLayout : PullBaseLayout, ListLoading, OnRefreshListener {
    var listView: PullToRefreshListView? = null
        private set
    private var mPullClickListener: PullClickListener? = null
    private var moreLayout: RelativeLayout? = null
    private var footLayout: RelativeLayout? = null
    private var moreText: TextView? = null

    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun childInit(view: View, context: Context) {
        listView = view
                .findViewById<View>(R.id.pull_refresh_list) as PullToRefreshListView

        moreLayout = LayoutInflater.from(context).inflate(
                R.layout.lnpull_base_loading_more, null) as RelativeLayout
        moreText = moreLayout!!.findViewById<View>(R.id.loadingMoreTv) as TextView
        moreLayout!!.setOnClickListener(this)
        listView!!.setOnRefreshListener(this)
        listView!!.setOnScrollListener(object : OnScrollListener {

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                // TODO Auto-generated method stub

            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int,
                                  visibleItemCount: Int, totalItemCount: Int) {
                // TODO Auto-generated method stub
                if (firstVisibleItem + visibleItemCount >= totalItemCount
                        && totalItemCount > 0 && mPullClickListener != null) {
                    if (canLoader() && !mPullClickListener!!.onMoreClick()) {
                        moreText!!.setText(R.string.pull_loading_more)
                    }
                }

            }
        })

    }

    override fun getContentView(context: Context): View? {
        // TODO Auto-generated method stub
        return LayoutInflater.from(context).inflate(
                R.layout.lnpull_base_loading_layout, null)
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        if (v === moreLayout) {
            if (mPullClickListener != null && moreText!!
                            .text
                            .toString()
                            .trim { it <= ' ' } == resources
                            .getString(R.string.pull_click_more)
                            .toString().trim { it <= ' ' }) {
                moreText!!.setText(R.string.pull_loading_more)
                mPullClickListener!!.onMoreClick()
            }
        } else {
            when (v.id) {
                R.id.errorImage -> if (mPullClickListener != null) {
                    startLoading()
                    mPullClickListener!!.onRetry()
                }
            }
        }
    }

    fun showLoadingMore() {
        if (footLayout == null) {
            footLayout = moreLayout
            listView!!.addFooterView(footLayout)
        }
        moreText!!.setText(R.string.pull_loading_more)
    }

    fun hideLoadingMore() {
        if (footLayout != null) {
            listView!!.removeFooterView(footLayout)
            footLayout = null
        }
    }

    fun showClickMore() {
        if (footLayout == null) {
            footLayout = moreLayout
            listView!!.addFooterView(footLayout)
        }
        moreText!!.setText(R.string.pull_click_more)
    }

    fun setOnPullClickListener(listener: PullClickListener) {
        this.mPullClickListener = listener
    }

    interface PullClickListener {
        fun onRefrensh()

        fun onRetry()

        fun onMoreClick(): Boolean

        fun onRefrenshPause()
    }

    override fun onRefresh() {
        // TODO Auto-generated method stub
        if (mPullClickListener != null) {
            mPullClickListener!!.onRefrensh()
        }
    }

    fun canLoader(): Boolean {
        val text = resources.getString(R.string.pull_loading_more)
        return listView!!.footerViewsCount > 0 && moreText!!.text.toString().trim { it <= ' ' } == text.trim { it <= ' ' }
    }

    override fun onRefrenshPause() {
        // TODO Auto-generated method stub
        if (mPullClickListener != null) {
            mPullClickListener!!.onRefrenshPause()
        }
    }
}
