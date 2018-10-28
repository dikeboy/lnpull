package com.lin.lnpull;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * author : leo
 * date   : 2018/10/2814:32
 */
public interface LoadingViewInter {
    public View getLoadingView();
    public View getErrorView();
    public View getEmptyView();
    public View getContentView();
}
