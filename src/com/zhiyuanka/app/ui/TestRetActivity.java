package com.zhiyuanka.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yichou.common.sdk.SdkUtils;
import com.yichou.common.utils.HttpUtils;
import com.yichou.common.utils.LocalDataUtils;
import com.zhiyuanka.app.R;
import com.zhiyuanka.app.data.Globals;

/**
 * 测试结果
 * 
 * @author Yichou
 * 
 */
public class TestRetActivity extends PostActivity implements OnClickListener, OnPageChangeListener {
	static final int NUM_ITEMS = 3;

	MyAdapter mAdapter;
	ViewPager mPager;
	TextView[] titles = new TextView[NUM_ITEMS];
	RadarFragment radarFragment;
	ProfFragment proFragment;
	ImageView imageView;
	DisplayMetrics metrics;
	LayoutParams params;
	

	static {
		LOADING_MSG = "数据加载中...";
	}
	
	@Override
	protected void onPost() throws Exception {
		StringBuilder builder = new StringBuilder(Globals.PATH + "/api/submitResult?");
		builder.append("uid=" + Globals.uid + "&");
		builder.append("e=" + Globals.mbti.getE() + "&");
		builder.append("s=" + Globals.mbti.getS() + "&");
		builder.append("t=" + Globals.mbti.getT() + "&");
		builder.append("j=" + Globals.mbti.getJ());
		
		Log.i("", builder.toString());
		
		String jsonRet = HttpUtils.get(this, builder.toString());
		if(jsonRet != null){
			JSONObject jsonObject = new JSONObject(jsonRet);
			int ret2 = jsonObject.getInt("error");

			if(ret2 == 0){
				Globals.rid = jsonObject.getInt("rid");
				Globals.mbtiJsonObj = jsonObject;
				LocalDataUtils.toPrivate(this, "mbti_" + Globals.uid, jsonRet);
				
				notifyPostSuc();
				return; //唯一一种成功情况
			}
		}

		notifyPostFail();
	}

	@Override
	protected void onPostSuc() {
		if(radarFragment != null)
			radarFragment.setData();
		if(proFragment != null)
			proFragment.setData();
	}
	
	private void readData() {
		String jsonRet = LocalDataUtils.getStringFromPrivate(this, "mbti_" + Globals.uid);
		if(jsonRet != null){
			try {
				JSONObject jsonObject = new JSONObject(jsonRet);
				int ret2 = jsonObject.getInt("error");
				
				if(ret2 == 0){
					Globals.rid = jsonObject.getInt("rid");
					Globals.mbtiJsonObj = jsonObject;
					
					SdkUtils.getSdk().sendEvent(getActivity(), 
							"finishTest", 
							"uid=" + Globals.uid
							 + "&rid=" + Globals.rid);
					
					return; //唯一一种成功情况
				}
			} catch (JSONException e) {
			}
		}
		
		post();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		readData();

		setContentView(R.layout.activity_test_ret_continer);

		mAdapter = new MyAdapter(getSupportFragmentManager(), this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);
		
		titles[0] = (TextView)findViewById(R.id.tv_radar);
		titles[0].setOnClickListener(this);
		titles[1] = (TextView)findViewById(R.id.tv_prof);
		titles[1].setOnClickListener(this);
		titles[2] = (TextView)findViewById(R.id.tv_report);
		titles[2].setOnClickListener(this);
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setAlpha(0xa0);
		
		metrics = getResources().getDisplayMetrics();
		params = (LayoutParams) imageView.getLayoutParams();
		params.width = metrics.widthPixels/3;
		params.leftMargin = 0;
		imageView.setLayoutParams(params);
		
		mPager.setCurrentItem(0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_radar: {
			mPager.setCurrentItem(0);
			break;
		}
		case R.id.tv_prof: {
			mPager.setCurrentItem(1);
			break;
		}
		case R.id.tv_report: {
			mPager.setCurrentItem(2);
			break;
		}
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		TestRetActivity activity;
		
		public MyAdapter(FragmentManager fm, TestRetActivity activity) {
			super(fm);
			this.activity = activity;
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				activity.radarFragment = new RadarFragment();
				return activity.radarFragment;
				
			case 1:
				activity.proFragment = new ProfFragment();
				return activity.proFragment;
				
			case 2:
				return new ReportFragment();
			}
			
			return null;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
//		System.out.println("1+" + arg0);
	}


	/**
	 * arg0 :当前页面，及你点击滑动的页面
	 * 
	 * arg1:当前页面偏移的百分比
	 * 
	 * arg2:当前页面偏移的像素位置
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		params.leftMargin = (int) ((arg0 + arg1)*params.width);
//		System.out.println("2>>" + arg0 + "," + arg1 + "," + arg2 + "," + params.leftMargin);
		imageView.setLayoutParams(params);
	}


	int curPage = 0;
	//滑动到莫一页，或者手动设置到某一页，会回调改方法，第一页是 0
	@Override
	public void onPageSelected(int page) {
		System.out.println("page = " + page);
		
		if(curPage != page){
//			titles[curPage].setCompoundDrawables(null, null, null, null);
//			titles[page].setCompoundDrawables(null, null, null, Res.selunderDrawable);
		}
		curPage = page;
	}
}
