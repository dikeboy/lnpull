package com.lin.lnpull;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lin.lnpull.ListLoading;

import java.util.List;

/**
 * author : leo
 * date   : 2018/10/2814:13
 */
public abstract  class PullBaseLayout extends LinearLayout implements LoadingViewInter,ListLoading {
	private View loadView;
	private View errorView;
	private View emptyView;
	private View contentView;

	public PullBaseLayout(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public PullBaseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullBaseLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		initAllView(context);
		loadView = getLoadingView();
		errorView = getErrorView();
		emptyView=getEmptyView();
		contentView = getContentView();
		startLoading();
	}


	public abstract void  initAllView(Context context);

	@Override
	public void startLoading() {
		// TODO Auto-generated method stub
		loadView.setVisibility(View.VISIBLE);
		errorView.setVisibility(View.GONE);
		contentView.setVisibility(View.INVISIBLE);
		emptyView.setVisibility(View.GONE);
	}

	@Override
	public void finishLoading() {
		// TODO Auto-generated method stub
		contentView.setVisibility(View.VISIBLE);
		loadView.setVisibility(View.VISIBLE);
		errorView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
	}

	@Override
	public void finishLoadingNoAnimation() {
		// TODO Auto-generated method stub
		contentView.setVisibility(View.VISIBLE);
		loadView.setVisibility(View.GONE);
		errorView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
	}

	@Override
	public void showErrorView() {
		// TODO Auto-generated method stub
		loadView.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		contentView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
	}

	@Override
	public void showEmptyView() {
		// TODO Auto-generated method stub
		loadView.setVisibility(View.GONE);
		errorView.setVisibility(View.GONE);
		contentView.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
	}
}
