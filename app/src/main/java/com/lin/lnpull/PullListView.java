package com.lin.lnpull;

import java.util.HashMap;
import java.util.Map;

import android.widget.BaseAdapter;
import com.lin.lnpull.PullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * author : leo
 * date   : 2018/10/2814:13
 */
@SuppressLint("NewApi")
public class PullListView extends PullBaseDetailLayout implements ListLoading, OnRefreshListener {
	private PullToRefreshListView listView;
	private PullClickListener mPullClickListener;
	private RelativeLayout moreLayout;
	private RelativeLayout footLayout;
	private TextView moreText;
	private Map<Integer,Boolean> loader = new HashMap<Integer,Boolean>();
	
	public PullListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void childInit(View view,Context context) {
		listView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);

		moreLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.lnpull_base_loading_more, null);
		moreText = (TextView) moreLayout.findViewById(R.id.loadingMoreTv);
		moreLayout.setOnClickListener(this);
		listView.setOnRefreshListener(this);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
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
		});

	}

	@Override
	public View getContentView(Context context) {
		// TODO Auto-generated method stub
		return LayoutInflater.from(context).inflate(
				R.layout.lnpull_base_loading_layout, null);
	}
	public PullToRefreshListView getListView() {
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
			listView.addFooterView(footLayout);
		}
		moreText.setText(R.string.pull_loading_more);
	}

	public void hideLoadingMore() {
		if (footLayout != null) {
			listView.removeFooterView(footLayout);
			footLayout = null;
		}
	}

	public void showClickMore() {
		if (footLayout == null) {
			footLayout = moreLayout;
			listView.addFooterView(footLayout);
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

	public boolean canLoader() {
		String text = getResources().getString(R.string.pull_loading_more);
		return listView.getFooterViewsCount() > 0
				&& moreText.getText().toString().trim().equals(text.trim());
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

	public void setAdapter(BaseAdapter adapter){
		listView.setAdapter(adapter);
	}
}
