package com.zhiyuanka.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyuanka.app.R;
import com.zhiyuanka.app.data.Tab_Province;


/**
 * 下拉列表数据库适配器
 * 
 * @author Yichou 2013-6-22
 *
 */
public final class SpinnerCursorAdapter extends CursorAdapter {
	private LayoutInflater inflater;

	public SpinnerCursorAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((TextView)view).setText(Tab_Province.getName(cursor));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		TextView textView = (TextView) inflater.inflate(R.layout.spinner_item, null);
		textView.setText(Tab_Province.getName(cursor));
		
		return textView;
	}
}