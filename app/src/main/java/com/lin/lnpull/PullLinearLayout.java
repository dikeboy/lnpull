package com.lin.lnpull;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.lin.lnpull.PullToScrollView.OnRefreshListener;

/**
 * author : leo
 * date   : 2018/10/2814:13
 */
public class PullLinearLayout extends PullBaseDetailLayout implements OnRefreshListener{
	private PullClickListener mPullClickListener;
	
	public PullLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public View getMainView(Context context) {
		// TODO Auto-generated method stub
		return LayoutInflater.from(context).inflate(
				R.layout.lnpull_base_loading, null);
	}
	

	public void addChildView(View view) {
		// TODO Auto-generated method stub
		if (view != null) {
			LayoutParams param = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			contentView.addView(view, param);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefrenshPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getContentView(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onRetry() {
		// TODO Auto-generated method stub
		if (mPullClickListener != null) {
			mPullClickListener.onRetry();
		}
	}
	public void setOnPullClickListener(PullClickListener listener) {
		this.mPullClickListener = listener;
	}
	public interface PullClickListener {
		public void onRetry();
	}
}
