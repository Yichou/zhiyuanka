package com.zhiyuanka.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyuanka.app.R;
import com.zhiyuanka.app.data.Globals;
import com.zhiyuanka.app.data.Res;

/**
 * 性格报告
 * 
 * @author Yichou 2013-6-22
 *
 */
public class ReportFragment extends BaseFragment implements OnClickListener {
	TextView tvContent;
	TextView selectedTextView;
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_btn_tezheng:
			try {
				JSONObject json = Globals.mbtiJsonObj.getJSONObject("mbti");
				tvContent.setText(json.getString("ext1"));
			} catch (JSONException e) {
			}
			break;
		case R.id.tv_btn_youshi:
			try {
				JSONObject json = Globals.mbtiJsonObj.getJSONObject("mbti");
				tvContent.setText(json.getString("ext2"));
			} catch (JSONException e) {
			}
			break;
		case R.id.tv_btn_leishi:
			try {
				JSONObject json = Globals.mbtiJsonObj.getJSONObject("mbti");
				tvContent.setText(json.getString("ext3"));
			} catch (JSONException e) {
			}
			break;
		case R.id.tv_btn_jianyi:
			try {
				JSONObject json = Globals.mbtiJsonObj.getJSONObject("mbti");
				tvContent.setText(json.getString("ext6"));
			} catch (JSONException e) {
			}
			break;
		}
		
		if(v != selectedTextView){
			selectedTextView.setCompoundDrawables(null, null, null, null);
			selectedTextView = (TextView)v;
			Res.selunderDrawable.setBounds(0, 0, selectedTextView.getWidth(), Res.getPix(3));
			selectedTextView.setCompoundDrawables(null, null, null, Res.selunderDrawable);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report, container, false);
		
		tvContent = (TextView) view.findViewById(R.id.tv_content);
		
		view.findViewById(R.id.tv_btn_tezheng).setOnClickListener(this);
		view.findViewById(R.id.tv_btn_youshi).setOnClickListener(this);
		view.findViewById(R.id.tv_btn_leishi).setOnClickListener(this);
		view.findViewById(R.id.tv_btn_jianyi).setOnClickListener(this);
		
		selectedTextView = (TextView) view.findViewById(R.id.tv_btn_tezheng);
		Res.selunderDrawable.setBounds(0, 0, selectedTextView.getWidth(), Res.getPix(3));
		selectedTextView.setCompoundDrawables(null, null, null, Res.selunderDrawable);
		onClick(selectedTextView);
		
		return view;
	}
}
