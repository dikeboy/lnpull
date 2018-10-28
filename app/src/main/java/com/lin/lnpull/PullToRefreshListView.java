package com.lin.lnpull;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * author : leo
 * date   : 2018/10/2814:13
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

    private static final int TAP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    
    private static final int SCROLL_POSITION_NIL=0;
    private static final int SCROLL_POSITION_VERTICAL=1;
    private static final int SCROLL_POSITION_HORIZONTAL=2;
    
    private static final String TAG = "PullToRefreshListView";

    private OnRefreshListener mOnRefreshListener;
    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    private RelativeLayout mRefreshView;
    private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;

    private int mCurrentScrollState;
    private int mRefreshState;

    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;

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

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
         mTouchSlop = configuration.getScaledTouchSlop();
        
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

        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        header =new PullListHeaderLayout(context);
		mRefreshView = (RelativeLayout) mInflater.inflate(
				R.layout.lnpull_to_refresh_header, this, false);
        mRefreshViewText =
            (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewImage =
            (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress =
            (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mRefreshViewLastUpdated =
            (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);
        mRefreshView.setOnClickListener(new OnClickRefreshListener());

        mRefreshState = TAP_TO_REFRESH;

        header.addView(mRefreshView);
        addHeaderView(header,null,false);


        super.setOnScrollListener(this);

        RelativeLayout refrenshView =  (RelativeLayout) mInflater.inflate(
				R.layout.lnpull_to_refresh_header, this, false);
        measureView(refrenshView);
        mRefreshViewHeight = refrenshView.getMeasuredHeight();
    }


    /**
     * Set the listener that will receive notifications every time the list
     * scrolls.
     * 
     * @param l The scroll listener. 
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    /**
     * Register a callback to be invoked when this list should be refreshed.
     * 
     * @param onRefreshListener The callback to run.
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * Set a text to represent when the list was last updated. 
     * @param lastUpdated Last updated at.
     */
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }
	private int mActivePointerId;
	private boolean isReset;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	
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
                if(getFirstVisiblePosition()==0&&headerHeight>0){
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
                else if(getFirstVisiblePosition()==0){
                	post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							headerHeight = header.getHeight();
			                if(getFirstVisiblePosition()==0&&headerHeight>0){
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
//            	Log.e("lin","onTouchMove ="+y+",height="+" last="+mLastMotionY);
            	if(mLastMotionY<y&&(!enable_horizontaltouch||scroll_position == SCROLL_POSITION_VERTICAL)&&getFirstVisiblePosition()==0){
            		int dis = (y -mLastMotionY)/2;
            		if(dis>mRefreshViewHeight&&mRefreshState==PULL_TO_REFRESH){
                          mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
                          mRefreshState = RELEASE_TO_REFRESH;
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mFlipAnimation);
            		}
            		else if(mRefreshState == TAP_TO_REFRESH){
                        mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
                        mRefreshState = PULL_TO_REFRESH;
                        mRefreshViewImage.setVisibility(View.VISIBLE);
                      mRefreshViewImage.clearAnimation();
                      mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                      event.setAction(MotionEvent.ACTION_CANCEL);
                      super.dispatchTouchEvent(event);
                      event.setAction(MotionEvent.ACTION_MOVE);
            		}
            		else if(dis<mRefreshViewHeight&&mRefreshState == RELEASE_TO_REFRESH){
                        mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
                        mRefreshState = PULL_TO_REFRESH;
                        mRefreshViewImage.setVisibility(View.VISIBLE);
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
            		}
                	header.setHeaderHeight(dis);
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
                pointerIndex = event.findPointerIndex(mActivePointerId);
                
                if (pointerIndex == -1) {
                    pointerIndex = 0;
                    mActivePointerId = event.getPointerId(pointerIndex);
                }
                
                y = (int) event.getY(pointerIndex);
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
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
    	if(firstVisibleItem>0&&mRefreshState==REFRESHING){
    		resetHeader();
    		if(mOnRefreshListener!=null){
    			mOnRefreshListener.onRefrenshPause();
    		}
    	}
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;

        if (mCurrentScrollState == SCROLL_STATE_IDLE) {
            mBounceHack = false;
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }
    public void prepareForRefresh() {
        mRefreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);
        // Set refresh view text to the refreshing label
        mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);
        mRefreshState = REFRESHING;
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh");

        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    /**
     * Resets the list to a normal state after a refresh.
     * @param lastUpdated Last updated at.
     */
    public void onRefreshComplete(CharSequence lastUpdated) {
        setLastUpdated(lastUpdated);
        onRefreshComplete();
    }

    /**
     * Resets the list to a normal state after a refresh.
     */
    public void onRefreshComplete() {        
        Log.d(TAG, "onRefreshComplete");

        // If refresh view is visible when loading completes, scroll down to
        // the next item.
        if (getFirstVisiblePosition() == 0&&mRefreshState ==REFRESHING) {
            space=(mRefreshViewHeight)/6;
            needScrollHeight = mRefreshViewHeight;
          	handler.post(new MRefrenshBackRunnable());
        }
    }
    /**
     * Invoked when the refresh view is clicked on. This is mainly used when
     * there's only a few items in the list and it's not possible to drag the
     * list.
     */
    private class OnClickRefreshListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (mRefreshState != REFRESHING) {
                prepareForRefresh();
                onRefresh();
            }
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
	   if(!bol)
		   removeHeaderView(mRefreshView);
   }
   
   public boolean  isRefrenshEnable(){
	   return enableRefrensh;
   }
   
   public void setEnableHorizontalTouch(boolean touch){
	   this.enable_horizontaltouch = touch;
   }
	   @Override
	public boolean performItemClick(View view, int position, long id) {
		// TODO Auto-generated method stub
		  if(position<getHeaderViewsCount()){
			  return true;
		  }
		  if(getAdapter()!=null&&(position>=getCount()-getFooterViewsCount()))
				  return true;
		return super.performItemClick(view, position-getHeaderViewsCount(), id);
	}
}
