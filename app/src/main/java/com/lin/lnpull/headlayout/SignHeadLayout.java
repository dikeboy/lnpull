package com.lin.lnpull.headlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lin.lnpull.R;

/**
 * author : leo
 * date   : 2018/10/3117:06
 */
public class SignHeadLayout implements BasicHeaderLayout {
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private RelativeLayout mRefreshView;
    private LayoutInflater mInflater;

    private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;

    public SignHeadLayout(Context context){

        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mRefreshView = (RelativeLayout) mInflater.inflate(
                R.layout.lnpull_to_refresh_header, null, false);
        mRefreshViewText =
                (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewImage =
                (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress =
                (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mRefreshViewLastUpdated =
                (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180,0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation.setFillBefore(true);
    }

    public View getRefrenshView(){
        return mRefreshView;
    }
    @Override
    public void pullDownAnim() {
        mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
        mRefreshViewImage.setVisibility(View.VISIBLE);
        mRefreshViewImage.clearAnimation();
        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
    }

    @Override
    public void pushUpAnim() {
        mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
        mRefreshViewImage.clearAnimation();
        mRefreshViewImage.startAnimation(mFlipAnimation);
    }

    @Override
    public void refrenshAnim() {

    }

    @Override
    public void reset() {
        mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
        // Replace refresh drawable with arrow drawable
        mRefreshViewImage.setImageResource(R.drawable.lnpull_refresh_arrow_down
        );
        // Clear the full rotation animation
        mRefreshViewImage.clearAnimation();
        // Hide progress bar and arrow.
        mRefreshViewImage.setVisibility(View.GONE);
        mRefreshViewProgress.setVisibility(View.GONE);
    }

    @Override
    public void onRefrensh() {
        mRefreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);
        // Set refresh view text to the refreshing label
        mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);
    }

    @Override
    public void onRefreshComplete() {
    }

}
