package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HistoryListAdapter extends BaseAdapter {
	private ArrayList<ImageView> items;

	public HistoryListAdapter() {
		this.items = new ArrayList<ImageView>();
	}

	public void add(
			ImageView v) {
		items.add(v);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(
			int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(
			int arg0) {
		return arg0;
	}

	@Override
	public View getView(
			int arg0,
			View arg1,
			ViewGroup arg2) {
		return items.get(arg0);
	}

	public void release() {
		if (items != null) {
			for (ImageView image : items) {
				image.setImageBitmap(null);
			}
			items.clear();
			items = null;
		}
	}

}
