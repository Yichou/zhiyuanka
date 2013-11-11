package com.zhiyuanka.app.ui;

import org.json.JSONObject;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.yichou.common.sdk.SdkUtils;
import com.yichou.common.utils.HttpUtils;
import com.zhiyuanka.app.R;
import com.zhiyuanka.app.adapter.SchoolListAdapter;
import com.zhiyuanka.app.adapter.SpinnerCursorAdapter;
import com.zhiyuanka.app.bean.School;
import com.zhiyuanka.app.data.Globals;
import com.zhiyuanka.app.widget.LoaderListView;
import com.zhiyuanka.app.widget.LoaderListView.FOOTVIEW_TYPE;
import com.zhiyuanka.app.widget.LoaderListView.LoadNotifyer;

/**
 * 学校列表
 * 
 * @author Yichou
 *
 */
public class SchoolListActivity extends PostActivity implements OnItemClickListener,
	SearchView.OnQueryTextListener,
	OnCheckedChangeListener,
	OnItemSelectedListener,
	View.OnClickListener,
	LoadNotifyer,
	SearchView.OnSuggestionListener {
	
	LoaderListView listView;
	SchoolListAdapter adapter;
	SuggestionsAdapter mSuggestionsAdapter;
	
	
	static {
		LOADING_MSG = "正在获取列表...";
	}
	
	final class Filter {
		String from_prov; //生源地
		String school_prov; //学校地
		String stu_type; //文/理科
		String level; //批次
		int pro_id; //职业id
		int school_id; //学校id
		int limit; //可以为空，默认返回10条
		int page; //当前第几页
		int uid; //用户id

		private Filter() {
			this.from_prov = null;
			this.school_prov = null;
			this.stu_type = null;
			this.level = null;
			this.pro_id = -100;
			this.school_id = -100;
			this.limit = -100;
			this.page = 1;
			this.uid = -100;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			if(from_prov != null) sb.append("&from_prov=" + from_prov);
			if(school_prov != null) sb.append("&school_prov=" + school_prov);
			if(stu_type != null) sb.append("&stu_type=" + stu_type);
			if(level != null) sb.append("&level=" + level);
			
			if(pro_id != -100) sb.append("&pro_id=" + pro_id);
			if(school_id != -100) sb.append("&school_id=" + school_id);
			if(limit != -100) sb.append("&limit=" + limit);
			if(page != -100) sb.append("&page=" + page);
			
			if(sb.charAt(0) == '&') //跳过开头 &
				return sb.substring(1);
			else
				return sb.toString();
		}
	}
	
	Filter filter = new Filter();
	PopupWindow filterWindow;
	
	@Override
	protected void onPost() throws Exception {
		StringBuilder builder = new StringBuilder(Globals.PATH + "/api/filter?");
		
		builder.append(filter.toString());
		
		Log.i("", builder.toString());
		
		String jsonRet = HttpUtils.get(this, builder.toString());
		if(jsonRet != null){
			JSONObject jsonObject = new JSONObject(jsonRet);
			int ret2 = jsonObject.getInt("error");

			if(ret2 == 0){
				adapter.setData(jsonObject);
				notifyPostSuc();
				
				return; //唯一一种成功情况
			}
		}

		notifyPostFail();
	}

	@Override
	protected void onPostSuc() {
		if(adapter.noMore){
			Toast.makeText(this, "没有更多了！", Toast.LENGTH_SHORT).show();
			listView.setFootviewType(FOOTVIEW_TYPE.NOMOR);
		}else {
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void load() {
		filter.page = adapter.page + 1;
		post();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		filter.pro_id = intent.getIntExtra("pro_id", -100);
		filter.from_prov = Globals.province;
		filter.level = Globals.pici;
		filter.stu_type = Globals.wenli;
		getSupportActionBar().setTitle(intent.getStringExtra("pro_name"));
		
		setContentView(R.layout.activity_school_list);
		
		listView = (LoaderListView) findViewById(R.id.listView1);
		adapter = new SchoolListAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setLoadNotifyer(this);
		
		post();
	}
	
	@Override
	protected void onDestroy() {
		adapter.recyle();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean isLight = false;
		
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("输入学校名");
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);

//		MenuItem item = menu.add(0, 0, 0, "查找");
//		item.setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.abs__ic_search);
//		item.setActionView(searchView);
//		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		
        menu.add(0, 1, 1, "过滤").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == 1001){
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.filter, null);
			
			RadioGroup radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
			radioGroup1.setOnCheckedChangeListener(this);
			
			Spinner spinner0 = (Spinner) view.findViewById(R.id.spinner0);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
					R.array.arr_pici, 
					android.R.layout.simple_spinner_item);
			
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner0.setAdapter(adapter);
			spinner0.setOnItemSelectedListener(this);
			
			Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
			spinner1.setAdapter(new SpinnerCursorAdapter(this, Globals.peovinceCursor));
			spinner1.setOnItemSelectedListener(this);
			
			Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);
			spinner2.setAdapter(new SpinnerCursorAdapter(this, Globals.peovinceCursor));
			spinner2.setOnItemSelectedListener(this);
			
			view.findViewById(R.id.btn_ok).setOnClickListener(SchoolListActivity.this);
			
			Dialog dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(view);
			return dialog;
		}else {
			return super.onCreateDialog(id);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			return true;
			
		case 1:
//			showFilter();
			showDialog(1001);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == R.id.spinner0) {
			filter.level = ((TextView)view).getText().toString();
		}else if(parent.getId() == R.id.spinner1){
			filter.from_prov = ((TextView)view).getText().toString();
		}else if(parent.getId() == R.id.spinner2){
			filter.school_prov = ((TextView)view).getText().toString();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_ok){
			dismissDialog(1001);
			
			adapter.clear();
			listView.setFootviewType(FOOTVIEW_TYPE.LOADING);
			filter.page = 1;
			post();
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(group.getId() == R.id.radioGroup1){
			filter.stu_type = ((RadioButton)group.findViewById(checkedId)).getText().toString();
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position < adapter.getCount()){
			SchoolInfoActivity.mSchool = (School) adapter.getItem(position);
			Globals.wenli = filter.stu_type;
			Globals.province = filter.from_prov;
			
			SdkUtils.getSdk().sendEvent(getActivity(), 
					"viewSchool", 
					"uid=" + Globals.uid
					 + "&id=" + SchoolInfoActivity.mSchool.id);
			
			Intent intent = new Intent(this, SchoolInfoActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "You searched for: " + query, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        return false;
    }

    private class SuggestionsAdapter extends CursorAdapter {

        public SuggestionsAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv = (TextView) view;
            final int textIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
            tv.setText(cursor.getString(textIndex));
        }
    }
}
