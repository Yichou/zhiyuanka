package com.zhiyuanka.app.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyuanka.app.R;
import com.zhiyuanka.app.adapter.BaseListAdapter;
import com.zhiyuanka.app.bean.Specialty;


/**
 * 学校详情-》专业列表
 * 
 * @author Yichou
 *
 */
public class SpecialtyListFragment extends BaseFragment implements OnItemClickListener {
	ListView listView;
	MyAdapter adapter;
	SchoolInfoActivity activity;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.activity = (SchoolInfoActivity) activity;
		adapter = new MyAdapter(getActivity());
		adapter.setData(SchoolInfoActivity.mSchool.specialtyList);
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
		
		return view;
	}
	
	private class MyAdapter extends BaseListAdapter<Specialty> {
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
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
}
