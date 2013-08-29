package com.zhiyuanka.app.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyuanka.app.R;
import com.zhiyuanka.app.bean.Mbti;
import com.zhiyuanka.app.common.Settings;
import com.zhiyuanka.app.data.Globals;
import com.zhiyuanka.app.data.Tab_Exams;


/**
 * 测试
 * 
 * @author Yichou
 *
 */
public class TestActivity extends BaseActivity implements OnClickListener {
	static final String TAG = "TestActivity";
	
	TextView tvProgress, tvIndex, tvTitle, tvA, tvB;
	ImageView imageView;
	Cursor cursor;
	int nowIndex;
	int pCount, eCount, sCount, fCount;
	
	int resultCounts;
	
	
	protected void post() {
		Settings.hasSucTest = true;
		Settings.save(this);
		Settings.saveTestRet(this);
		
		startActivity(new Intent(this, TestRetActivity.class));
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_test);
		
		tvProgress = (TextView) findViewById(R.id.tv_progress);
		tvIndex = (TextView) findViewById(R.id.tv_num);
		tvA = (TextView) findViewById(R.id.tv_ansa);
		tvB = (TextView) findViewById(R.id.tv_ansb);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		findViewById(R.id.btn_ansa).setOnClickListener(this);
		findViewById(R.id.btn_ansb).setOnClickListener(this);
		
		initTest();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		cursor.close();
	}
	
	private void initTest() {
		cursor = Globals.dbUtil.getExams();
		
		if(cursor == null)
			return;
		
		resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			// 读取数据失败
		}
		
		nowIndex = 1;
		pCount = eCount = sCount = fCount = 0;

		tvTitle.setText(Tab_Exams.getTitle(cursor));
		tvA.setText(Tab_Exams.getAnsA(cursor));
		tvB.setText(Tab_Exams.getAnsB(cursor));
		tvIndex.setText(String.valueOf(nowIndex) + ".");
		tvProgress.setText(nowIndex + "/" + resultCounts);
	}
	
	private void next() {
//		post();
		
		if (cursor.moveToNext()) {// && nowIndex <= 45
			nowIndex++;
			
			String title = Tab_Exams.getTitle(cursor);
			tvTitle.setText((title==null || title.length()==0)? "你更趋向于" : title);
			tvA.setText(Tab_Exams.getAnsA(cursor));
			tvB.setText(Tab_Exams.getAnsB(cursor));
			tvIndex.setText(String.valueOf(nowIndex) + ".");
			tvProgress.setText(nowIndex + "/" + resultCounts);
		}else {
			Globals.mbti = new Mbti(Math.round(50*pCount/8.0f), 
					Math.round(50*eCount/8.0f), 
					Math.round(50*sCount/8.0f), 
					Math.round(50*fCount/8.0f));
			Log.e(TAG, Globals.mbti.toString());
			
			post();
		}
	}
	
	private void addType(String type) {
		switch (type.charAt(0)) {
		case 'p': pCount++; break;
		case 'e': eCount++; break;
		case 's': sCount++; break;
		case 'f': fCount++; break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ansa:
			addType(Tab_Exams.getTypeA(cursor));
			next();
			break;
			
		case R.id.btn_ansb:
			addType(Tab_Exams.getTypeB(cursor));
			next();
			break;
		}
	}
}
