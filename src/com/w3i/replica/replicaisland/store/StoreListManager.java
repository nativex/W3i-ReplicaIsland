package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w3i.replica.replicaisland.R;

public class StoreListManager {
	private ArrayList<ListItem> items;
	private LinearLayout itemsList;

	public StoreListManager(LinearLayout itemsList) {
		this.itemsList = itemsList;
		items = new ArrayList<ListItem>();
	}

	public void addListItem(Item<?> item) {
		addListItem(item, null);
	}

	public void addListItem(Item<?> item, View.OnClickListener listener) {
		ListItem listItem = new ListItem();
		items.add(listItem);
		listItem.setItem(item);
		listItem.setOnClickListener(listener);
	}

	public class ListItem {
		private Item<?> item;
		private ViewGroup itemLayout;

		private ListItem() {
		}

		private ListItem(Item<?> item) {
			this.item = item;
			setItem(item);
		}

		public void setItem(Item<?> item) {
			LayoutInflater li = (LayoutInflater) itemsList.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (ViewGroup) li.inflate(R.layout.store_item, null);

			ImageView iconView = (ImageView) itemLayout
					.findViewById(R.id.itemIcon);
			TextView nameView = (TextView) itemLayout
					.findViewById(R.id.itemName);
			TextView descView = (TextView) itemLayout
					.findViewById(R.id.itemDescription);
			TextView priceView = (TextView) itemLayout
					.findViewById(R.id.itemPrice);

			String priceLabel = itemsList.getContext().getResources()
					.getString(R.string.store_item_price_label);

			iconView.setBackgroundDrawable(item.getIcon());
			nameView.setText(item.getName());
			descView.setText(item.getDescription());
			priceView.setText(priceLabel + item.getPrice());
			itemLayout.setTag(item);
			itemsList.addView(itemLayout);
		}

		public void setOnClickListener(View.OnClickListener listener) {
			itemLayout.setOnClickListener(listener);
		}

		public Item<?> getItem() {
			return item;
		}

		public void release() {
			if (itemLayout != null) {
				ImageView iconView = (ImageView) itemLayout
						.findViewById(R.id.itemIcon);
				iconView.setBackgroundDrawable(null);
				itemLayout.setOnClickListener(null);
				itemLayout.removeAllViews();
				itemLayout = null;
			}
		}
	}

	public void release() {
		for (ListItem i : items) {
			i.release();
		}
		itemsList.removeAllViews();
	}
}
