package com.zhiyuanka.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 
 * @author Yichou
 *
 */
public abstract class BaseListAdapter<E> extends BaseAdapter {
	protected  LayoutInflater inflater;
	protected ArrayList<E> list;
	
	
	public BaseListAdapter(Context context) {
		list = new ArrayList<E>();
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<E> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void addItem(E object) {
		list.add(object);
	}
}
