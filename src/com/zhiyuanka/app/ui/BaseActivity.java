package com.zhiyuanka.app.ui;

import android.app.Activity;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.yichou.common.sdk.SdkUtils;
import com.zhiyuanka.app.common.Settings;
import com.zhiyuanka.app.data.Globals;
import com.zhiyuanka.app.data.Res;

public abstract class BaseActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(Res.actbarBgDrawable == null){ //说明崩溃重启了
//			System.exit(1);
			Res.init(this);
			Globals.init(this);
			Settings.read(this);
//			return;
		}
		
		super.onCreate(savedInstanceState);

		getSupportActionBar().setBackgroundDrawable(Res.actbarBgDrawable);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public Activity getActivity() {
		return this;
	}
	
	@Override
	protected void onPause() {
		SdkUtils.getSdk().onPause(getActivity());
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SdkUtils.getSdk().onResume(getActivity());
	}
}
