package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.replica.replicaisland.R;

public class ReplicaStoreManager {
	private ArrayList<StoreItem> storeItems;
	private LinearLayout itemsList;
	private GridView historyList;
	private ArrayList<HistoryItem> historyItems;
	private HistoryListAdapter adapter;

	private View.OnClickListener onStoreItemClicked = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Object o = arg0.getTag();
			if (o instanceof StoreItem) {
				StoreItem item = (StoreItem) o;
				Toast.makeText(itemsList.getContext(),
						item.getItem().getDisplayName() + " clicked",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private View.OnClickListener onHistoryItemClicked = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Object o = arg0.getTag();
			if (o instanceof HistoryItem) {
				HistoryItem item = (HistoryItem) o;
				Toast.makeText(itemsList.getContext(),
						item.getItem().getDisplayName() + " clicked",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	public ReplicaStoreManager(LinearLayout itemsList, GridView historyList) {
		this.itemsList = itemsList;
		storeItems = new ArrayList<StoreItem>();
		historyItems = new ArrayList<HistoryItem>();
		this.historyList = historyList;
		adapter = new HistoryListAdapter();
		historyList.setAdapter(adapter);
		for (Item i : GamesPlatformManager.getItems()) {
			Log.e("Store Item: " + (i == null ? "is null" : i.getDisplayName()));
			addStoreItem(i, onStoreItemClicked);
			addHistoryItem(i, onHistoryItemClicked);
		}
	}

	public void addStoreItem(Item item) {
		addStoreItem(item, onStoreItemClicked);
	}

	public void addStoreItem(Item item, View.OnClickListener listener) {
		StoreItem listItem = new StoreItem();
		storeItems.add(listItem);
		listItem.setItem(item);
		listItem.setOnClickListener(listener);
	}

	public void addHistoryItem(Item item, View.OnClickListener listener) {
		HistoryItem historyItem = new HistoryItem();
		historyItems.add(historyItem);
		historyItem.addItem(item);
		historyItem.setOnClickListener(listener);
	}

	public class StoreItem {
		private Item storeItem;
		private ViewGroup itemLayout;
		private Map<Currency, Double> prices;
		CustomImageView iconView;
		TextView nameView;
		TextView descView;
		TextView priceView;
		String priceLabel;

		private StoreItem() {
			init();
		}

		private StoreItem(Item item) {
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
		}

		public void setItem(Item item) {
			this.storeItem = item;
			if (item == null) {
				Log.e("ReplicaStoreManager: Store Item is null");
				return;
			}
			prices = item.getItemPrice(GamesPlatformManager.getCurrencies());
			iconView.setImageFromInternet(item.getStoreImageUrl());
			nameView.setText(item.getDisplayName());
			descView.setText(item.getDescription());
			setPrice(0);

			itemLayout.setTag(this);
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
			return storeItem;
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

	public class HistoryItem {
		private CustomImageView icon;
		private Item historyItem;

		public HistoryItem() {
			icon = new CustomImageView(historyList.getContext());
		}

		public HistoryItem(Item item) {
			this();
			addItem(item);
		}

		public void addItem(Item item) {
			this.historyItem = item;
			if (item == null) {
				Log.e("StoreManager: History Item is null");
			}
			icon.setImageFromInternet(item.getStoreImageUrl());
			adapter.add(icon);
		}

		public Item getItem() {
			return historyItem;
		}

		public void release() {
			if (icon != null) {
				icon.setImageDrawable(null);
				icon.setOnClickListener(null);
				icon = null;
			}
		}

		public void setOnClickListener(View.OnClickListener listener) {
			icon.setOnClickListener(listener);
		}
	}

	public void release() {

		if (storeItems != null) {
			for (StoreItem i : storeItems) {
				i.release();
			}
			storeItems.clear();
			storeItems = null;
		}
		if (historyItems != null) {
			for (HistoryItem i : historyItems) {
				i.release();
			}
			historyItems.clear();
			historyItems = null;
		}
		if (itemsList != null) {
			itemsList.removeAllViews();
		}
		if (adapter != null) {
			adapter.release();
			adapter = null;
		}
	}
}
