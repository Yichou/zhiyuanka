package com.zhiyuanka.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 下拉自动加载的 Listview 
 * 
 * @author Yichou
 * @创建日期 2013-3-19 16:01:10
 * 
 * 2013-6-30
 */
public class LoaderListView extends ListView implements 
		OnScrollListener, 
		OnItemClickListener,
		OnClickListener {
	public interface LoadNotifyer {
		public void load();
	}
	
	public interface OnScrollStateChangedListener {
		public void onScrollStateChanged(int oldState, int newState);
	}
	
	private LinearLayout footViewLoading, footViewRetry, footViewNomore;
	private LoadNotifyer loadNotifyer;
	private int scrollState;
	private OnScrollStateChangedListener onScrollStateChangedListener;
	

	public LoaderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LoaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoaderListView(Context context) {
		super(context);
		init(context);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == 0x1001){ //重新加载
			setFootviewType(FOOTVIEW_TYPE.LOADING);
			if(loadNotifyer != null)
				loadNotifyer.load();
		}else if (v.getId() == 0x1002) {
			setSelection(0);
		}
	}
	
	private void init(Context context) {
		footViewLoading = new LinearLayout(context);
		footViewLoading.setOrientation(LinearLayout.HORIZONTAL);
		footViewLoading.setGravity(Gravity.CENTER);
		ProgressBar bar = new ProgressBar(context);
		TextView textView = new TextView(context);
		textView.setText("加载中...");
		footViewLoading.addView(bar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		footViewLoading.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		footViewRetry = new LinearLayout(context);
		footViewRetry.setOrientation(LinearLayout.HORIZONTAL);
		footViewRetry.setGravity(Gravity.CENTER);
		textView = new TextView(context);
		textView.setId(0x1001);
		textView.setGravity(Gravity.CENTER);
		textView.setText("网络不给力，请重试 o(︶︿︶)o");
		textView.setOnClickListener(this);
		footViewRetry.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, getFixPx(50)));
		
		footViewNomore = new LinearLayout(context);
		footViewNomore.setOrientation(LinearLayout.HORIZONTAL);
		footViewNomore.setGravity(Gravity.CENTER);
		footViewNomore.setId(0x1002);

		textView = new TextView(context);
		textView.setText("返回顶部↑");
		textView.setGravity(Gravity.CENTER);
		
		footViewNomore.setClickable(true);
		footViewNomore.setOnClickListener(this);
		footViewNomore.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, getFixPx(50)));
		
		setFootviewType(FOOTVIEW_TYPE.LOADING);
		
		setOnScrollListener(this);
		scrollState = SCROLL_STATE_IDLE;
		
		super.setOnItemClickListener(this);
	}
	
	public enum FOOTVIEW_TYPE {
		/** 加载中 */
		LOADING, 
		/** 没有更多了，返回顶部 */
		NOMOR, 
		/** 加载失败重试 */
		RETRY,
		/**无*/
		NONE
	}
	
	private View curFootView;
	public void setFootviewType(FOOTVIEW_TYPE type) {
		if(curFootView != null && curFootView.getTag() == type)
			return;
		
		if(curFootView != null)
			removeFooterView(curFootView);
		
		switch (type) {
		case LOADING:
			curFootView = footViewLoading;
			break;
		case NOMOR:
			curFootView = footViewNomore;
			break;
		case RETRY:
			curFootView = footViewRetry;
			break;
		case NONE:
			return;
		}
		
		addFooterView(curFootView);
		curFootView.setTag(type);
	}

	private View curHeadView;
	public void setHeadView(View v) {
		if(curHeadView!=null)
			return;
		curHeadView=v;
		addHeaderView(v);
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState != this.scrollState) {
			if(onScrollStateChangedListener != null){
				onScrollStateChangedListener.onScrollStateChanged(this.scrollState, scrollState);
			}
			this.scrollState = scrollState;
		}
	}

	protected int firstVisibleItem, visibleItemCount, totalItemCount;
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if(totalItemCount < 2 ) //footview 也算
			return;
		
//		System.out.println("first=" + firstVisibleItem + ",visible=" + visibleItemCount + ",total=" + totalItemCount);
		
		if(firstVisibleItem + visibleItemCount >= totalItemCount){ //说明 footView 可见，通知加载更多
			if (loadNotifyer != null && (curFootView != footViewNomore)) {
				loadNotifyer.load();
			}
		}
		this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
	}
	
	public int getFirstVisibleItem() {
		return firstVisibleItem;
	}
	
	public int getVisibleItemCount() {
		return visibleItemCount;
	}
	
	public int getScrollState() {
		return scrollState;
	}
	
	public void setLoadNotifyer(LoadNotifyer loadNotifyer) {
		this.loadNotifyer = loadNotifyer;
	}
	
	public void setOnScrollStateChangedListener(OnScrollStateChangedListener onScrollStateChangedListener) {
		this.onScrollStateChangedListener = onScrollStateChangedListener;
	}
	
	public int getFixPx(int dp){
		float scale=getContext().getResources().getDisplayMetrics().density;
		return (int)(scale*dp+0.5);
	}

	private OnItemClickListener listener;
	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
//		super.setOnItemClickListener(listener);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(listener==null)return;
		if(curHeadView != null){
			if(position==0)return;
			listener.onItemClick(parent, view, position-1, id);
		}else{
			listener.onItemClick(parent, view, position, id);
		}
	}
}
