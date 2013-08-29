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
import com.zhiyuanka.app.widget.RadarView;


/**
 * 性格雷达
 * 
 * @author Yichou 2013-6-22
 *
 */
public class RadarFragment extends BaseFragment implements OnClickListener {
	RadarView radarView;
	TextView tvName, tvTitle, tvSummary, tvDescript, tvExpand;
	

	boolean b;
	@Override
	public void onClick(View v) {
		tvExpand.setText(b? "收缩" : "展开");
		tvDescript.setMaxLines(b? 100 : 3);
		b = !b;
	}
	
	public void setData() {
		try {
			JSONObject json = Globals.mbtiJsonObj.getJSONObject("mbti");
			
			tvName.setText(json.getString("name"));
			tvSummary.setText(json.getString("summary"));
			tvDescript.setText(json.getString("ext1"));
			tvTitle.setText(json.getString("ext7"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_radar, container, false);
		
		tvName = (TextView) view.findViewById(R.id.tv_mbti_name);
		tvTitle = (TextView) view.findViewById(R.id.tv_mbti_title);
		tvSummary = (TextView) view.findViewById(R.id.tv_mbti_short_desc);
		tvDescript = (TextView) view.findViewById(R.id.tv_mbti_desc);
		tvDescript.setOnClickListener(this);
		tvExpand = (TextView) view.findViewById(R.id.tv_expand);
		tvExpand.setOnClickListener(this);
		radarView = (RadarView) view.findViewById(R.id.radarView1);
		
		if(Globals.mbtiJsonObj != null)
			setData();
		
		return view;
	}
	
	
}
