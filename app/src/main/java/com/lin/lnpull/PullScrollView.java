package com.lin.lnpull;


import com.lin.lnpull.PullToScrollView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * author : leo
 * date   : 2018/10/2814:13
 */
@SuppressLint("NewApi")
public class PullScrollView  extends PullBaseDetailLayout implements OnRefreshListener {
	private PullToScrollView scrollView;
	private PullClickListener mPullClickListener;
	public PullScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public View getMainView(Context context) {
		// TODO Auto-generated method stub
		return LayoutInflater.from(context).inflate(
				R.layout.lnpull_base_loading, null);
	}
	
	@Override
	public void childInit(View view, Context context) {
		// TODO Auto-generated method stub
		super.childInit(view, context);
	}
	public void addChildView(View view) {
		// TODO Auto-generated method stub
		if (view != null) {
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			scrollView.addMChildView(view, param);
		}
	}
	
	@Override
	public View getContentView(Context context) {
		// TODO Auto-generated method stub
		scrollView =  (PullToScrollView) LayoutInflater.from(context).inflate(
				R.layout.lnpull_base_normal_refrensh_layout, null);
		scrollView.setOnRefreshListener(this);
		return scrollView;
	}
	
	public PullToScrollView getScrollView() {
		return scrollView;
	}
	
	public interface PullClickListener {
		public void onRefrensh();

		public void onRetry();
	}
	public void setOnPullClickListener(PullClickListener listener) {
		this.mPullClickListener = listener;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (mPullClickListener != null) {
			mPullClickListener.onRefrensh();
		}
	}

	@Override
	public void onRetry() {
		// TODO Auto-generated method stub
		if (mPullClickListener != null) {
			mPullClickListener.onRetry();
		}
	}

	@Override
	public void onRefrenshPause() {
		// TODO Auto-generated method stub
		if (mPullClickListener != null) {
		}
	}

}
