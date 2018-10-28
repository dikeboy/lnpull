package com.lin.lnpull;

import android.view.View;
import android.widget.LinearLayout.LayoutParams;
/**
 * author : leo
 * date   : 2018/10/2814:13
 */
public interface ListLoading {
	public View  getLoadingView();
	public View  getErrorView();
	public void startLoading();
	public void finishLoading();
	public void finishLoadingNoAnimation();
	public void  showErrorView();
	public void showEmptyView();
	public void addBottomView(View view);
	public void addBottomView(View view, LayoutParams params);
}
