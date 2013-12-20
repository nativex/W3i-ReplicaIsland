package com.recharge.torch.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nativex.common.Log;
import com.nativex.monetization.custom.views.CustomImageView;
import com.recharge.torch.R;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.upgrades.Upgrades;

public class HistoryListAdapter extends BaseAdapter {
	private List<Upgrade> items;

	public HistoryListAdapter() {
		items = new ArrayList<Upgrade>();
		reset();
	}

	public void reset() {
		items.clear();
		for (Upgrades upgrade : Upgrades.values()) {
			if (upgrade.isOwned()) {
				items.add(upgrade.getUpgrade());
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public int getCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	public Object getItem(
			int arg0) {
		if (items != null) {
			return items.get(arg0);
		}
		return null;
	}

	public long getItemId(
			int arg0) {
		return arg0;
	}

	public void clear() {
		reset();
	}

	public View getView(
			int arg0,
			View arg1,
			ViewGroup arg2) {
		CustomImageView item = null;
		try {
			if (arg1 == null) {
				LayoutInflater inflater = (LayoutInflater) arg2.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				item = (CustomImageView) inflater.inflate(R.layout.ui_store_history_item, null);
			} else {
				item = (CustomImageView) arg1;
			}
			Upgrade torchItem = items.get(arg0);
			item.setImageResource(torchItem.getIcon());
			item.setTag(torchItem);
		} catch (Exception e) {
			Log.e("HistoryAdapter: Exception caught while operating on item " + arg0, e);
		}
		return item;
	}
}
