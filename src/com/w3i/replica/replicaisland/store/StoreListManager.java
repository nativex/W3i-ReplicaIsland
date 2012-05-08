package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.replica.replicaisland.R;

public class StoreListManager {
	private ArrayList<ListItem> items;
	private LinearLayout itemsList;

	public StoreListManager(LinearLayout itemsList) {
		this.itemsList = itemsList;
		items = new ArrayList<ListItem>();
		for (Item i : GamesPlatformManager.getItems()) {
			addListItem(i);
		}
	}

	public void addListItem(Item item) {
		addListItem(item, null);
	}

	public void addListItem(Item item, View.OnClickListener listener) {
		ListItem listItem = new ListItem();
		items.add(listItem);
		listItem.setItem(item);
		listItem.setOnClickListener(listener);
	}

	public class ListItem {
		private Item item;
		private ViewGroup itemLayout;
		private Map<Currency, Double> prices;
		CustomImageView iconView;
		TextView nameView;
		TextView descView;
		TextView priceView;
		String priceLabel;

		private ListItem() {
		}

		private ListItem(Item item) {
			this.item = item;
			init();
			setItem(item);
		}

		private void init() {
			LayoutInflater li = (LayoutInflater) itemsList.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (ViewGroup) li.inflate(R.layout.store_item, null);

			iconView = (CustomImageView) itemLayout.findViewById(R.id.itemIcon);
			nameView = (TextView) itemLayout.findViewById(R.id.itemName);
			descView = (TextView) itemLayout.findViewById(R.id.itemDescription);
			priceView = (TextView) itemLayout.findViewById(R.id.itemPrice);

			priceLabel = itemsList.getContext().getResources()
					.getString(R.string.store_item_price_label);
			prices = item.getItemPrice(GamesPlatformManager.getCurrencies());
		}

		public void setItem(Item item) {
			iconView.setImageFromInternet(item.getStoreImageUrl());
			nameView.setText(item.getDisplayName());
			descView.setText(item.getDescription());
			setPrice(0);

			itemLayout.setTag(item);
			itemsList.addView(itemLayout);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			itemLayout.setLayoutParams(params);
		}

		public void setPrice(int index) {
			priceView.setText(priceLabel
					+ prices.get(GamesPlatformManager.getCurrencies()
							.get(index)));
		}

		public void setOnClickListener(View.OnClickListener listener) {
			itemLayout.setOnClickListener(listener);
		}

		public Item getItem() {
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
