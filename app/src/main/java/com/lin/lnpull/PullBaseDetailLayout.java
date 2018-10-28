package com.lin.lnpull;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

/**
 * author : leo
 * date   : 2018/10/2814:27
 */
public abstract class PullBaseDetailLayout extends PullBaseLayout implements View.OnClickListener {
    protected RelativeLayout loadView;
    protected RelativeLayout errorView;
    protected RelativeLayout emptyView;
    protected LinearLayout contentView;
    private ImageView loadImage;
    private Context context;
    private TextView emptyTv;
    private ImageView errorImageView;

    public PullBaseDetailLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public PullBaseDetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullBaseDetailLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initAllView(Context context) {
        this.context = context;
        View view = getMainView(context);

        loadView = (RelativeLayout) view.findViewById(R.id.listLoadingLayout);
        errorView = (RelativeLayout) view.findViewById(R.id.listErrorLayout);
        emptyView=(RelativeLayout)view.findViewById(R.id.listEmptyLayout);
        contentView = (LinearLayout) view.findViewById(R.id.mainLayout);

        View content=getContentView(context);
        if(content!=null){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0);
            params.weight=1;
            contentView.addView(content,params);
        }
        loadImage = (ImageView) loadView.findViewById(R.id.loadingImage);
        errorImageView = (ImageView) view.findViewById(R.id.errorImage);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, params);

        errorImageView.setOnClickListener(this);
        view.findViewById(R.id.empty_image).setOnClickListener(this);
        childInit(view,context);
    }

    public   View getMainView(Context context){
        return LayoutInflater.from(context).inflate(
                R.layout.lnpull_base_loading, null);
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    public View getEmptyView() {
        return emptyView;
    }

    @Override
    public View getErrorView() {
        return errorView;
    }

    @Override
    public View getLoadingView() {
        return loadView;
    }

    @Override
    public void startLoading() {
        super.startLoading();
        final AnimationDrawable animation = (AnimationDrawable) loadImage
                .getDrawable();
        loadImage.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                animation.start();
            }
        });
    }

    @Override
    public void finishLoading() {
        super.finishLoading();
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(context,
                R.anim.lnpull_fade_out);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadView.setVisibility(View.GONE);
                ((AnimationDrawable) loadImage.getDrawable()).stop();
                loadView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        loadView.startAnimation(fadeOutAnimation);
    }

    @Override
    public void addBottomView(View view) {
        // TODO Auto-generated method stub
        if (view != null) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            contentView.addView(view, param);
        }
    }
    @Override
    public void addBottomView(View view,LinearLayout.LayoutParams params) {
        // TODO Auto-generated method stub
        if (view != null) {
            contentView.addView(view, params);
        }
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.errorImage:
                startLoading();
                onRetry();
                break;
        }
    }
    public void  childInit(View view,Context context){};
    public abstract View  getContentView(Context context);
    public abstract void  onRetry();

    public void setEmptyText(String text){
        if(emptyTv!=null){
            emptyTv.setText(text);
        }
    }
}
