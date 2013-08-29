package com.zhiyuanka.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.umeng.fb.FeedbackAgent;
import com.yichou.sdk.DownloadCallback;
import com.yichou.sdk.SdkUtils;
import com.zhiyuanka.app.R;
import com.zhiyuanka.app.common.Settings;
import com.zhiyuanka.app.common.UmenSdkImpl;
import com.zhiyuanka.app.data.Globals;
import com.zhiyuanka.app.data.Res;

public class HomeActivity extends BaseActivity implements OnClickListener {
	FeedbackAgent agent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!Settings.hasSucTest) { // 未完整测试过则提示用户先测试
			setContentView(R.layout.activity_home0);
			findViewById(R.id.btn_start_test).setOnClickListener(this);

		} else {
			setContentView(R.layout.activity_home);
			findViewById(R.id.tv_btn_about).setOnClickListener(this);
			findViewById(R.id.tv_btn_testret).setOnClickListener(this);
			findViewById(R.id.tv_btn_retest).setOnClickListener(this);
			findViewById(R.id.tv_btn_fadeback).setOnClickListener(this);
		}
		
		SdkUtils.setRealImpl(new UmenSdkImpl(this));
		
		SdkUtils.enableCrashHandle(this, true);
		SdkUtils.updateOnlineParams(this);
		SdkUtils.setUpdateCfg(false, true);
		SdkUtils.setDownloadCallback(new DownloadCallback() {
			@Override
			public void OnDownloadRet(int ret) {
				if(ret != DownloadCallback.RET_SUCCESS)
					Toast.makeText(getActivity(), "更新包下载失败，错误码" + ret , Toast.LENGTH_SHORT).show();
			}
		});
		
		SdkUtils.checkUpdate(getActivity());
		
		agent = new FeedbackAgent(getActivity());
		agent.sync();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Res.recycle();
		Globals.recyle();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_start_test) {
			startActivity(new Intent(this, UsersetActivity.class));
		} else if (v.getId() == R.id.tv_btn_about) {
			startActivity(new Intent(this, AboutActivity.class));
		} else if (v.getId() == R.id.tv_btn_retest) {
			startActivity(new Intent(this, UsersetActivity.class));
		} else if (v.getId() == R.id.tv_btn_testret) {
			Settings.readTestRet(this);
			startActivity(new Intent(this, TestRetActivity.class));
		} else if (v.getId() == R.id.tv_btn_fadeback) {
		    agent.startFeedbackActivity();
		}
	}
}
