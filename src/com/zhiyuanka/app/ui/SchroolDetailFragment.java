package com.zhiyuanka.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zhiyuanka.app.R;

/**
 * 学校详情页
 * 
 * @author Yichou 2013-7-20
 *
 */
public class SchroolDetailFragment extends BaseFragment implements OnClickListener {
	TextView tvDetail, tvHomeUrl;
	SchoolInfoActivity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (SchoolInfoActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_school_detail, container, false);

		tvDetail = (TextView) view.findViewById(R.id.tv_detail);
		tvHomeUrl = (TextView) view.findViewById(R.id.tv_school_home_url);
		tvHomeUrl.setOnClickListener(this);
		
		tvHomeUrl.setText(SchoolInfoActivity.mSchool.url);
		tvDetail.setText(activity.jsonObject.optString("school_detail"));

		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_school_home_url:
			startActivity(new Intent(Intent.ACTION_VIEW, 
					Uri.parse(((TextView)v).getText().toString())));
			break;
		}
	}
}
