package com.zhiyuanka.app.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyuanka.app.R;
import com.zhiyuanka.app.bean.School;

/**
 * 学校列表适配器
 * 
 * @author Yichou
 *
 */
public final class SchoolListAdapter extends BaseAdapter {
	public LayoutInflater inflater;
	public ArrayList<School> schoolList;
	public int page;
	public int limit;
	public boolean noMore = false;
	
	Context mContext;
	

	public void setData(JSONObject jsonObject) {
		try {
			page = jsonObject.getInt("page");
			limit = jsonObject.getInt("limit");
			
			JSONArray array = jsonObject.getJSONArray("rs_list");
			int count = array.length();
			if(count <= 0){ //没有了
				noMore = true;
				return;
			}
			
			for (int i = 0; i < count; i++) {
				JSONObject obj = array.getJSONObject(i);
				School school = new School();
				
				school.parseInfo(obj);
				
				schoolList.add(school);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void recyle() {
		for(School s : schoolList){
			s.recyle();
		}
	}
	
	public SchoolListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		schoolList = new ArrayList<School>();
		mContext = context;
	}
	
	public ArrayList<School> getSchoolList() {
		return schoolList;
	}
	
	public void clear() {
		schoolList.clear();
		noMore = false;
		page = 1;
		limit = 10;
	}

	@Override
	public int getCount() {
		return schoolList.size();
	}

	@Override
	public Object getItem(int position) {
		return schoolList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.school_list_item, null);
			convertView.setTag(holder);
			
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_school_icon);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvType1 = (TextView) convertView.findViewById(R.id.tv_type1);
			holder.tvType2 = (TextView) convertView.findViewById(R.id.tv_type2);
//			holder.tvMajro1 = (TextView) convertView.findViewById(R.id.tv_majro1);
//			holder.tvMajro2 = (TextView) convertView.findViewById(R.id.tv_majro2);
//			holder.tvMajro3 = (TextView) convertView.findViewById(R.id.tv_majro3);
//			holder.tvMajro4 = (TextView) convertView.findViewById(R.id.tv_majro4);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final School school = schoolList.get(position);
		
		holder.tvName.setText(school.name);
		holder.tvType1.setText(school.property1 + school.property2);
		holder.tvType2.setText(school.type);
		
		school.getIcon(mContext, holder.ivIcon);
		
//		if(school.specialtyList.size() > 0)
//			holder.tvMajro1.setText(school.specialtyList.get(0).specialty_category);
//		if(school.specialtyList.size() > 1)
//			holder.tvMajro2.setText(school.specialtyList.get(1).specialty_category);
//		if(school.specialtyList.size() > 2)
//			holder.tvMajro3.setText(school.specialtyList.get(2).specialty_category);
//		if(school.specialtyList.size() > 3)
//			holder.tvMajro4.setText(school.specialtyList.get(3).specialty_category);
		
		return convertView;
	}

	final class ViewHolder {
		ImageView ivIcon;
		TextView tvName, tvType1, tvType2;
//		TextView tvMajro1, tvMajro2, tvMajro3, tvMajro4;
	}
}
