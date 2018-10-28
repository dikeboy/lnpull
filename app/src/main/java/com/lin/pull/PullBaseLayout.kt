package com.lin.pull


import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.lin.lnpull.R
/**
 * author : leo
 * date   : 2018/10/2812:07
 */
abstract class PullBaseLayout : LinearLayout, ListLoading, View.OnClickListener {
    private lateinit var loadView: RelativeLayout
    private  lateinit var errorView: RelativeLayout
    private  lateinit var emptyView: RelativeLayout
    lateinit  var mainLayout: LinearLayout
    lateinit var loadImage: ImageView
    lateinit var mContext: Context
    private lateinit var errorImageView: ImageView
    private var mPullClickListener: PullClickListener? = null

    constructor(context: Context) : super(context) {
        init(context)
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context): View {
        this.mContext = context
        val view = getMainView(context)
        loadView = view.findViewById(R.id.listLoadingLayout)
        errorView = view.findViewById(R.id.listErrorLayout)
        emptyView = view.findViewById(R.id.listEmptyLayout)
        mainLayout = view.findViewById(R.id.mainLayout)
        val content = getContentView(context)
        if (content != null) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
            params.weight = 1f
            mainLayout?.addView(content,params)
        }
        loadImage = loadView!!.findViewById(R.id.loadingImage) as ImageView
        errorImageView = view.findViewById(R.id.errorImage) as ImageView
        val params = FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        addView(view, params)

        errorImageView!!.setOnClickListener(this)
        childInit(view, context)
        startLoading()
        return view
    }


    open fun childInit(view: View, context: Context) {

    }

     fun getErrorView(): View? {
        // TODO Auto-generated method stub
        return errorView
    }

    override fun startLoading() {
        // TODO Auto-generated method stub
        loadView!!.visibility = View.VISIBLE
        errorView!!.visibility = View.GONE
        mainLayout!!.visibility = View.GONE
        emptyView!!.visibility = View.GONE
        val animation = loadImage!!
                .drawable as AnimationDrawable
        loadImage!!.post {
            // TODO Auto-generated method stub
            animation.start()
        }

    }

    override fun finishLoading() {
        // TODO Auto-generated method stub
        mainLayout!!.visibility = View.VISIBLE
        loadView!!.visibility = View.VISIBLE
        errorView!!.visibility = View.GONE
        emptyView!!.visibility = View.GONE
        val fadeOutAnimation = AnimationUtils.loadAnimation(context,
                R.anim.fade_out)
        fadeOutAnimation.setAnimationListener(object : AnimationListener {

            override fun onAnimationStart(animation: Animation) {
                // TODO Auto-generated method stub
            }

            override fun onAnimationRepeat(animation: Animation) {
                // TODO Auto-generated method stub

            }

            override fun onAnimationEnd(animation: Animation) {
                // TODO Auto-generated method stub
                loadView!!.visibility = View.GONE
                (loadImage!!.drawable as AnimationDrawable).stop()
                loadView!!.clearAnimation()
            }
        })
        loadView!!.startAnimation(fadeOutAnimation)
    }

    override fun finishLoadingNoAnimation() {
        // TODO Auto-generated method stub
        mainLayout!!.visibility = View.VISIBLE
        loadView!!.visibility = View.GONE
        errorView!!.visibility = View.GONE
        emptyView!!.visibility = View.GONE
    }

    override fun showErrorView() {
        // TODO Auto-generated method stub
        loadView!!.visibility = View.GONE
        errorView!!.visibility = View.VISIBLE
        mainLayout!!.visibility = View.GONE
        emptyView!!.visibility = View.GONE
    }

    override fun showEmptyView() {
        // TODO Auto-generated method stub
        loadView!!.visibility = View.GONE
        errorView!!.visibility = View.GONE
        mainLayout!!.visibility = View.GONE
        emptyView!!.visibility = View.VISIBLE
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        when (v.id) {
            R.id.errorImage -> if (mPullClickListener != null) {
                startLoading()
                mPullClickListener!!.onRetry()
            }
        }
    }

    fun setOnPullClickListener(listener: PullClickListener) {
        this.mPullClickListener = listener
    }

    interface PullClickListener {
        fun onRetry()
    }

    override fun addBottomView(view: View) {
        // TODO Auto-generated method stub
            val param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            mainLayout?.addView(view, param)

    }

    override fun addBottomView(view: View, params: LinearLayout.LayoutParams) {
        // TODO Auto-generated method stub
            mainLayout?.addView(view, params)

    }

    open fun getMainView(context: Context): View {
        return LayoutInflater.from(context).inflate(
                R.layout.lnpull_base_loading, null)
    }

    open fun getContentView(context: Context): View? {
        return null
    }
}
