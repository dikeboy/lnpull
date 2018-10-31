package com.lin.lnpull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.lin.lnpull.PullToRefreshListView.OnRefreshListener;
import com.lin.lnpull.headlayout.SignHeadLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * author : leo
 * date   : 2018/10/2814:13
 */
@SuppressLint("NewApi")
public class PullRecycleView extends PullBaseDetailLayout implements ListLoading, OnRefreshListener {
	private PullToRecycleView listView;
	private PullClickListener mPullClickListener;
	private RelativeLayout moreLayout;
	private RelativeLayout footLayout;
	private TextView moreText;
	private Map<Integer,Boolean> loader = new HashMap<Integer,Boolean>();

	public PullRecycleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullRecycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullRecycleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void childInit(View view,Context context) {
		listView = (PullToRecycleView) view
				.findViewById(R.id.pull_refresh_list);
		listView.setHeaderLayout(new SignHeadLayout(context));
		moreLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.lnpull_base_loading_more, null);
		moreText = (TextView) moreLayout.findViewById(R.id.loadingMoreTv);
		moreLayout.setOnClickListener(this);
		listView.setOnRefreshListener(this);
		listView.addOnScrollListener(new RecyclerView.OnScrollListener(){
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
					int visibleItemCount =recyclerView.getChildCount();
					int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
					int totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();

					if (firstVisibleItem + visibleItemCount >= totalItemCount
							&& totalItemCount > 0 && mPullClickListener != null) {
						if (canLoader()&&loader.get(firstVisibleItem+visibleItemCount)==null && !mPullClickListener.onMoreClick()){
							loader.put(firstVisibleItem+visibleItemCount, true);
							moreText.setText(R.string.pull_loading_more);
						}
					}
					else if(mPullClickListener!=null&&firstVisibleItem+visibleItemCount< totalItemCount-1&&loader.size()>0){
						moreText.setText(R.string.pull_loading_more);
						loader.clear();
					}

				}

			}
		});


	}

	@Override
	public View getContentView(Context context) {
		// TODO Auto-generated method stub
		return LayoutInflater.from(context).inflate(
				R.layout.lnpull_base_recycle_layout, null);
	}
	public PullToRecycleView getListView() {
		return listView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == moreLayout) {
			if (mPullClickListener != null
					&& moreText
							.getText()
							.toString()
							.trim()
							.equals(getResources()
									.getString(R.string.pull_click_more)
									.toString().trim())) {
				moreText.setText(R.string.pull_loading_more);
				mPullClickListener.onMoreClick();
			}
		} else {
			switch (v.getId()) {
			case R.id.empty_image:
			case R.id.errorImage:
				if (mPullClickListener != null) {
					startLoading();
					mPullClickListener.onRetry();
				}
				break;
			}
		}
	}

	public void showLoadingMore() {
		if (footLayout == null) {
			footLayout = moreLayout;
			listView.getAdapter().showMore(true);
		}
		moreText.setText(R.string.pull_loading_more);
	}

	public void hideLoadingMore() {
		if (footLayout != null) {
			footLayout = null;
			listView.getAdapter().showMore(false);
		}
	}
	public boolean canLoader() {
		String text = getResources().getString(R.string.pull_loading_more);
		return footLayout!=null
				&& moreText.getText().toString().trim().equals(text.trim());
	}
	public void showClickMore() {
		if (footLayout == null) {
			footLayout = moreLayout;
			listView.getAdapter().showMore(true);
		}
		moreText.setText(R.string.pull_click_more);
	}

	public void setOnPullClickListener(PullClickListener listener) {
		this.mPullClickListener = listener;
	}

	public interface PullClickListener {
		public void onRefrensh();

		public void onRetry();

		public boolean onMoreClick();
		
		public void onRefrenshPause();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (mPullClickListener != null) {
			mPullClickListener.onRefrensh();
		}
	}


	@Override
	public void onRefrenshPause() {
		// TODO Auto-generated method stub
		if (mPullClickListener != null) {
			mPullClickListener.onRefrenshPause();
		}
	}

	@Override
	public void onRetry() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (mPullClickListener != null) {
			mPullClickListener.onRetry();
		}
	}
	public RelativeLayout getMoreLayout(){
		return moreLayout;
	}
	public void setAdapter(RecyclerView.Adapter adapter){
		listView.setAdapter(adapter);
	}
}
