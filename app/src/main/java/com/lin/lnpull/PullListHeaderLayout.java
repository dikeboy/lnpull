package com.lin.lnpull;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * author : leo
 * date   : 2018/10/2814:13
 */
public class PullListHeaderLayout extends  ViewGroup{
	public int mHeight;
	// The height when user release can trigger update.
	private int mUpdateHeight;
	
	public PullListHeaderLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    public PullListHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		if (mHeight < 0) {
			mHeight = 0;
		}
		setMeasuredDimension(width, mHeight);

		final View childView = getChildView();
		if (childView != null) {
			childView.measure(widthMeasureSpec, heightMeasureSpec);
			mUpdateHeight = childView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
			final View childView = getChildView();
			if (childView == null) {
				return;
			}

			final int childViewWidth = childView.getMeasuredWidth();
			final int childViewHeight = childView.getMeasuredHeight();
			final int measuredHeight = getMeasuredHeight();
			childView.layout(0, measuredHeight - childViewHeight, childViewWidth,
					measuredHeight);
	}
	
	protected View getChildView() {
		final int childCount = getChildCount();
		if (childCount != 1) {
			return null;
		}
		return getChildAt(0);
	}
	public void setHeaderHeight(int height) {
		if (mHeight == height && height == 0) {
			// ignore duplicate 0 height setting.
			return;
		}
		this.mHeight = height;
		Log.e("lin","height="+height);
		requestLayout();
	}
	@Override
	public void requestLayout() {
		// TODO Auto-generated method stub
		super.requestLayout();
	}
}
