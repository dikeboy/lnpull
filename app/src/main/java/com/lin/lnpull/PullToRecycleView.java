package com.lin.lnpull;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;
import com.lin.lnpull.headlayout.BasicHeaderLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author : leo
 * date   : 2018/10/2817:10
 */
public class PullToRecycleView extends RecyclerView implements  RefrenshState {
    private static final int TAP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;

    private static final int SCROLL_POSITION_NIL=0;
    private static final int SCROLL_POSITION_VERTICAL=1;
    private static final int SCROLL_POSITION_HORIZONTAL=2;

    private static final String TAG = "PullToRecycleView";

    private PullToRefreshListView.OnRefreshListener mOnRefreshListener;
    private AbsListView.OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;



    private int mCurrentScrollState;
    private int mRefreshState;

    private int mRefreshViewHeight;
    private int mLastMotionY;
    private int mLastMotionX;
    private static final int INVALID_POINTER_ID = -1;

    private boolean mBounceHack;

    private boolean  isTouchDown;
    private boolean  enableBackAnimation=true;
    private boolean  enableRefrensh=true;

    private PullListHeaderLayout header;

    private int headerHeight;
    private int needScrollHeight;
    private int mTouchSlop;
    private int mScrollY;
    private int scroll_position = 0;
    private boolean enable_horizontaltouch;
    private Context context;
    private BasicHeaderLayout mSignHeadLayout;
    private View  selfHeadView;

    public PullToRecycleView(@NonNull Context context) {
        super(context, (AttributeSet)null);
        init(context);
    }

    public PullToRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }
    public PullToRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context) {
        this.context = context;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    public  PullListHeaderLayout getHeader(){
        if(header==null){
            header =new PullListHeaderLayout(context);
            mRefreshState = TAP_TO_REFRESH;
            header.addView(mSignHeadLayout.getRefrenshView());
            measureView(mSignHeadLayout.getRefrenshView());
            mRefreshViewHeight = mSignHeadLayout.getRefrenshView().getMeasuredHeight();
        }
        return header;
    }

    @Override
    public void setHeaderLayout(BasicHeaderLayout headerLayout) {
        this.mSignHeadLayout = headerLayout;
    }

    /**
     * Register a callback to be invoked when this list should be refreshed.
     *
     * @param onRefreshListener The callback to run.
     */
    public void setOnRefreshListener(PullToRefreshListView.OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    private int mActivePointerId;
    private boolean isReset;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
//        Log.e("lin","action="+event.getAction()+",state="+mRefreshState+",height="+ header.getHeight()+",isTouchDown="+isTouchDown
//        +",scrollY="+getLayoutManager().findViewByPosition(0));
        if(!enableRefrensh){
            return super.dispatchTouchEvent(event);
        }

        int y = (int) event.getY();
        mBounceHack = false;
        isReset = false;
        if(mRefreshState == REFRESHING){
            return super.dispatchTouchEvent(event);
        }
        if(!isTouchDown&&mRefreshState==RELEASE_TO_REFRESH){
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                headerHeight = header.getHeight();
//            	Log.e("lin","touchUp =="+getFirstVisiblePosition()+", headerHeight="+headerHeight);
                isTouchDown=false;
                scroll_position  =SCROLL_POSITION_NIL;
                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                if(headerHeight>0){
                    space=(headerHeight)/6;
                    if(headerHeight<mRefreshViewHeight){
                        needScrollHeight = headerHeight;
                        handler.post(new PullToRecycleView.MRefrenshBackRunnable());
                    }
                    else{
                        needScrollHeight  = headerHeight - mRefreshViewHeight;
                        handler.post(new PullToRecycleView.MFlingRunnable());
                    }
                }
                else if(headerHeight<=0){
                    post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            headerHeight = header.getHeight();
                            if(headerHeight>0){
                                space=(headerHeight)/6;
                                if(headerHeight<mRefreshViewHeight){
                                    needScrollHeight = headerHeight;
                                    handler.post(new MRefrenshBackRunnable());
                                }
                                else{
                                    needScrollHeight  = headerHeight - mRefreshViewHeight;
                                    handler.post(new MFlingRunnable());
                                }
                            }
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_DOWN:
//            	Log.e("lin","touch down");

                if(mRefreshState != TAP_TO_REFRESH){
                    headerHeight = header.getHeight();
                    if(mRefreshState==PULL_TO_REFRESH&&headerHeight==0){
                        mRefreshState = TAP_TO_REFRESH;
                    }
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return true;
                }
                mRefreshState = TAP_TO_REFRESH;
                mActivePointerId = event.getPointerId(0);
                mLastMotionY = y;
                isTouchDown=true;
                scroll_position  =SCROLL_POSITION_NIL;
                mLastMotionX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER_ID) {
                    break;
                }
                int pointerIndex = event.findPointerIndex(mActivePointerId);

                if (pointerIndex == -1) {
                    pointerIndex = 0;
                    mActivePointerId = event.getPointerId(pointerIndex);
                }

                y = (int) event.getY(pointerIndex);
                if(enable_horizontaltouch&&scroll_position  ==SCROLL_POSITION_NIL){
                    if(Math.abs(y-mLastMotionY)>mTouchSlop){
                        scroll_position = SCROLL_POSITION_VERTICAL;
                    }
                    else if(Math.abs(mLastMotionX-event.getX(pointerIndex)) > mTouchSlop){
                        scroll_position = SCROLL_POSITION_HORIZONTAL;
                    }
                }
                if(getLayoutManager().findViewByPosition(0)==null){
                    mLastMotionY=y;
                }
//            	Log.e("lin","onTouchMove ="+y+",height="+" last="+mLastMotionY);
                if(mLastMotionY<y&&(!enable_horizontaltouch||scroll_position == SCROLL_POSITION_VERTICAL)&&getScrollY()==0&&getLayoutManager().findViewByPosition(0)!=null){
                    int dis = (y -mLastMotionY)/2;
                    if(dis>mRefreshViewHeight&&mRefreshState==PULL_TO_REFRESH){
                        mRefreshState = RELEASE_TO_REFRESH;
                        mSignHeadLayout.pushUpAnim();
                    }
                    else if(mRefreshState == TAP_TO_REFRESH){
                        mRefreshState = PULL_TO_REFRESH;
                        mSignHeadLayout.pullDownAnim();
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        super.dispatchTouchEvent(event);
                        event.setAction(MotionEvent.ACTION_MOVE);
                    }
                    else if(dis<mRefreshViewHeight&&mRefreshState == RELEASE_TO_REFRESH){
                        mRefreshState = PULL_TO_REFRESH;
                        mSignHeadLayout.pullDownAnim();
                    }
                    header.setHeaderHeight(dis);
//                    Log.e("lin","走不动了" +",dis="+dis);
                    return true;
                }
                else if(y<=mLastMotionY&&mRefreshState!=TAP_TO_REFRESH){
                    resetHeader();
                    isReset = true;
//            		Log.e("lin","resetHeader");
                }
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN:
//    			Log.e("lin","onPointDown");
                if(!isTouchDown){
                    return super.dispatchTouchEvent(event);
                }
                final int index = MotionEventCompat.getActionIndex(event);
                y = (int) MotionEventCompat.getY(event, index);
                mLastMotionY = (int) (-y + event.getY())+mLastMotionY;
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
//    			Log.e("lin","onPointUp");
                onSecondaryPointerUp(event);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean startScrollIfNeeded(int y) {
        // Check if we have moved far enough that it looks more like a
        // scroll than a tap
        final int deltaY = y - mLastMotionY;
        final int distance = Math.abs(deltaY);
        final boolean overscroll = mScrollY != 0;
        if (overscroll || distance > mTouchSlop) {
            return true;
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = (int) (ev.getY(newPointerIndex) - ev.getY())+mLastMotionY;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//     	Log.e("lin","onTouchEvent ="+event.getAction()+" x="+event.getY()+" ,touchDownX "+mRefreshState);
        if(isTouchDown&&isReset&&event.getAction()==MotionEvent.ACTION_MOVE){
            event.setAction(MotionEvent.ACTION_CANCEL);
            super.onTouchEvent(event);
            event.setAction(MotionEvent.ACTION_DOWN);
            return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * Resets the header to the original state.
     */
    private void resetHeader() {
        if (mRefreshState != TAP_TO_REFRESH) {
            mRefreshState = TAP_TO_REFRESH;
            // Set refresh view text to the pull label
            header.setHeaderHeight(0);
            mSignHeadLayout.reset();
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


    public void prepareForRefresh() {
        mSignHeadLayout.onRefrensh();
        mRefreshState = REFRESHING;
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh");

        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    public void onRefreshComplete() {
        Log.d(TAG, "onRefreshComplete");

        // If refresh view is visible when loading completes, scroll down to
        // the next item.
        if (mRefreshState ==REFRESHING) {
            space=(mRefreshViewHeight)/6;
            needScrollHeight = mRefreshViewHeight;
            handler.post(new PullToRecycleView.MRefrenshBackRunnable());
        }
    }


    /**
     * Interface definition for a callback to be invoked when list should be
     * refreshed.
     */
    public interface OnRefreshListener {
        /**
         * Called when the list should be refreshed.
         * <p>
         * A call to {@link PullToRefreshListView #onRefreshComplete()} is
         * expected to indicate that the refresh has completed.
         */
        public void onRefresh();

        /**
         * called when  pull up to item more than 1
         */
        public void onRefrenshPause();
    }
    Handler handler=new Handler(Looper.getMainLooper());
    private float space;
    class MFlingRunnable implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(needScrollHeight>0&&space>0){
                needScrollHeight = (int) (needScrollHeight - space);
                if(needScrollHeight<0)
                    needScrollHeight=0;
                header.setHeaderHeight(needScrollHeight + mRefreshViewHeight);
                handler.postDelayed(this,10);
            }
            else{
                prepareForRefresh();
                onRefresh();
            }
        }
    }

    class MRefrenshBackRunnable implements Runnable{
        public void run(){
            if(needScrollHeight>0&&space>0){
                needScrollHeight = (int) (needScrollHeight - space);
                if(needScrollHeight<0)
                    needScrollHeight=0;
                header.setHeaderHeight(needScrollHeight);
                handler.postDelayed(this,10);
            }
            else{
                resetHeader();
            }
        }
    }
    public void setEnableBackAnimation(boolean bol){
        this.enableBackAnimation=bol;
    }
    public boolean getEnableBackAnimation(){
        return enableBackAnimation;
    }

    public void enableRefrensh(boolean bol){
        this.enableRefrensh=bol;

    }

    public boolean  isRefrenshEnable(){
        return enableRefrensh;
    }

    public void setEnableHorizontalTouch(boolean touch){
        this.enable_horizontaltouch = touch;
    }

    @Nullable
    @Override
    public PullRecycleAdapter getAdapter() {
        return (PullRecycleAdapter)super.getAdapter();
    }

    public void setSelfHeadView(View view){
       this.selfHeadView = view;
    }

    public int getHeaderViewCount(){
        int i = selfHeadView==null?0:1;
        if(enableRefrensh){
            return i+1;
        }
       return i;
    }

    public View getSelfHeadView(){
        return selfHeadView;
    }

    public void removeHeaderViews(){
        selfHeadView = null;
    }
}
