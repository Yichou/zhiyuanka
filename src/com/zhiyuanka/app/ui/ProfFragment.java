package com.zhiyuanka.app.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yichou.common.sdk.SdkUtils;
import com.zhiyuanka.app.R;
import com.zhiyuanka.app.adapter.BaseListAdapter;
import com.zhiyuanka.app.bean.Profession;
import com.zhiyuanka.app.data.Globals;

/**
 * 职业列表
 * 
 * @author Yichou 2013-6-22
 *
 */
public class ProfFragment extends BaseFragment implements OnItemClickListener {
	ListView listView;
	MyAdapter adapter;
	
	
	public void setData() {
		try {
			JSONArray array = Globals.mbtiJsonObj.getJSONArray("profession_list");
			int l = array.length();
			for (int i = 0; i < l; i++) {
				JSONObject obj = array.getJSONObject(i);
				adapter.addItem(new Profession(obj.getInt("id"), obj.getString("name")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		adapter = new MyAdapter(getActivity());
	}
	
	@Override
	public void onResume() {
		super.onResume();

		adapter.notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		listView = (ListView) view.findViewById(R.id.listView1);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
		
		if(Globals.mbtiJsonObj != null)
			setData();
		
		return view;
	}
	
	private class MyAdapter extends BaseListAdapter<Profession> {
		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.prof_list_item, null);
			}
			
			((TextView)convertView).setText(list.get(position).name);
			
			return convertView;
		}
		
		public int getId(int position) {
			return list.get(position).id;
		}
		
		public String getName(int position) {
			return list.get(position).name;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SdkUtils.getSdk().sendEvent(getActivity(), 
				"viewProfession", 
				"uid=" + Globals.uid
				 + "&pro_id=" + adapter.getId(position));
		
		Intent intent = new Intent(getActivity(), SchoolListActivity.class);
		intent.putExtra("pro_id", adapter.getId(position));
		intent.putExtra("pro_name", adapter.getName(position));
		startActivity(intent);
	}
}
