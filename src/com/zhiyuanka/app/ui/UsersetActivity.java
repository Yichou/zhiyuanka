package com.zhiyuanka.app.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.yichou.common.HttpUtils;
import com.yichou.sdk.SdkUtils;
import com.zhiyuanka.app.R;
import com.zhiyuanka.app.adapter.SpinnerCursorAdapter;
import com.zhiyuanka.app.common.Utils;
import com.zhiyuanka.app.data.Globals;

public class UsersetActivity extends PostActivity implements OnClickListener,
	OnItemSelectedListener {
	Spinner spinner;
	RadioGroup radioGroup1, radioGroup2;
	String province = "北京市";
	
	
	@Override
	protected void onPostSuc() {
		startActivity(new Intent(this, TestActivity.class));
	}
	
	@Override
	protected void onPost() throws Exception {
		StringBuilder builder = new StringBuilder(Globals.PATH + "/api/createUser?");
		builder.append("mobile=" + Utils.getPhoneNumber(this) + "&");
		
		RadioButton button = (RadioButton) radioGroup1.findViewById(radioGroup1.getCheckedRadioButtonId());
		Globals.wenli = button.getText().toString();
		builder.append("stu_type=" + Globals.wenli + "&");
		
		Globals.province = province;
		builder.append("from_prov=" + province + "&");
		
		button = (RadioButton) radioGroup2.findViewById(radioGroup2.getCheckedRadioButtonId());
		Globals.pici = button.getText().toString();
		builder.append("mark_cate=" + Globals.pici);
		
		Log.i("", builder.toString());
		
		String jsonRet = HttpUtils.get(builder.toString());
		if(jsonRet != null){
			JSONObject jsonObject = new JSONObject(jsonRet);
			int ret2 = jsonObject.getInt("error");

			if(ret2 == 0){
				Globals.uid = Integer.valueOf(jsonObject.getString("uid"));
				Log.i("", "getUid=" + Globals.uid);
				
				SdkUtils.event(getActivity(), 
						"createUser", 
						"uid=" + Globals.uid);
				
				notifyPostSuc();
				
				return; //唯一一种成功情况
			}
		}

		notifyPostFail();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler(this);

		setContentView(R.layout.activity_userset);
		findViewById(R.id.btn_enter).setOnClickListener(this);
		
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(new SpinnerCursorAdapter(this, Globals.peovinceCursor));
		spinner.setOnItemSelectedListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_enter){
			post();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(parent.getId() == R.id.spinner1){
			province = ((TextView)view).getText().toString();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
