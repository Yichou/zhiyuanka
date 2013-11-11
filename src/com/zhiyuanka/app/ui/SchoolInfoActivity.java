package com.zhiyuanka.app.ui;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yichou.common.utils.HttpUtils;
import com.zhiyuanka.app.R;
import com.zhiyuanka.app.bean.School;
import com.zhiyuanka.app.data.Globals;

/**
 * 学校详情
 * 
 * @author Yichou 2013-6-26
 * 
 */
public class SchoolInfoActivity extends PostActivity implements OnClickListener, OnPageChangeListener {
	public static School mSchool;

	TextView tvName, tvType1, tvType2;
	ImageView ivIcon;
	JSONObject jsonObject;
	ImageView imageView;
	MyAdapter mAdapter;
	ViewPager mPager;
	DisplayMetrics metrics;
	LayoutParams params;
	TextView tvBtn;

	static {
		LOADING_MSG = "正在获取数据...";
	}

	@Override
	protected void onPost() throws Exception {
		StringBuilder builder = new StringBuilder(Globals.PATH + "/api/school?");

		builder.append("id=" + mSchool.id);
		builder.append("&withpro=1");
		builder.append("&from_prov=" + Globals.province);
		builder.append("&stu_type=" + Globals.wenli);

		String jsonRet = HttpUtils.get(this, builder.toString());
		if (jsonRet != null) {
			JSONObject jsonObject = new JSONObject(jsonRet);
			int ret2 = jsonObject.getInt("error");

			if (ret2 == 0) {
				this.jsonObject = jsonObject;

				mSchool.parseSpecialties(jsonObject);

				notifyPostSuc();

				return; // 唯一一种成功情况
			}
		}

		notifyPostFail();
	}

	@Override
	protected void onPostSuc() {
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_info);

		tvName = (TextView) findViewById(R.id.tv_name);
		tvType1 = (TextView) findViewById(R.id.tv_type1);
		tvType2 = (TextView) findViewById(R.id.tv_type2);
		ivIcon = (ImageView) findViewById(R.id.iv_school_icon);
		tvBtn = (TextView) findViewById(R.id.tv_btn_btn);

		tvBtn.setOnClickListener(this);

		tvName.setText(mSchool.name);
		tvType1.setText(mSchool.property1 + mSchool.property2);
		tvType2.setText(mSchool.type);
		ivIcon.setImageBitmap(mSchool.iconBitmap);

		getSupportActionBar().setTitle(mSchool.name);

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);

		imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setAlpha(0xa0);
		metrics = getResources().getDisplayMetrics();
		params = (LayoutParams) imageView.getLayoutParams();
		params.width = metrics.widthPixels / 2;
		params.leftMargin = 0;
		imageView.setLayoutParams(params);

		post();
	}
	
	@Override
	protected void onDestroy() {
		mSchool.specialtyList.clear();
		mSchool = null;
		
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_btn_btn:
			mPager.setCurrentItem((curPage + 1)%2); //切换页面
			break;
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new SpecialtyListFragment();

			case 1:
				return new SchroolDetailFragment();
			}

			return null;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		params.leftMargin = (int) ((arg0 + arg1) * params.width);
		imageView.setLayoutParams(params);
	}

	int curPage = 0;
	@Override
	public void onPageSelected(int arg0) {
		curPage = arg0;
		if (arg0 == 0) {
			tvBtn.setText("专业列表");
		} else if (arg0 == 1) {
			tvBtn.setText("学校简介");
		}
	}

}
