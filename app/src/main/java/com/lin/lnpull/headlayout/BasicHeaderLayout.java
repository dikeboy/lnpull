package com.lin.lnpull.headlayout;

import android.view.View;

/**
 * author : leo
 * date   : 2018/10/3117:07
 */
public interface BasicHeaderLayout {
    public void pullDownAnim();
    public void pushUpAnim();
    public void refrenshAnim();
    public void reset();
    public void onRefrensh();
    public void onRefreshComplete();
    public View getRefrenshView();
}
