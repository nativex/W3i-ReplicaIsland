package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.AdvancedTextView;
import com.w3i.offerwall.custom.views.AdvancedTextView.Text;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.replica.replicaisland.R;

public class ReplicaStoreManager {
	private ArrayList<StoreItem> storeItems;
	private LinearLayout itemsList;
	private GridView historyList;
	private ArrayList<HistoryItem> historyItems;
	private HistoryListAdapter adapter;
	private ReplicaInfoDialog infoDialog;
	private StoreItem selectedItem = null;

	private static final int STORE_ITEM_PRICE_LABEL_COLOR = Color.GRAY;
	private static final int STORE_ITEM_PRICE_COLOR = Color.argb(255, 255, 229, 34);
	public static final int DEFAULT_CATEGORY_BACKGROUND_COLOR = Color.DKGRAY;
	public static final int DEFAULT_CATEGORY_TEXT_COLOR = Color.WHITE;

	private View.OnClickListener onPurchaseClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View arg0) {
			if (selectedItem != null) {
				Item item = selectedItem.getItem();
				PowerupManager.handleItem(item);
				GamesPlatformManager.storePurchasedItem(item.getId());
				addHistoryItem(item);
				selectedItem.release();
				selectedItem = null;
				if ((infoDialog != null) && (infoDialog.isShowing())) {
					infoDialog.dismiss();
				}
			}
		}
	};

	private View.OnClickListener onStoreItemClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View arg0) {
			Object o = arg0.getTag();
			if (o instanceof StoreItem) {
				StoreItem storeItem = (StoreItem) o;
				Item item = storeItem.getItem();
				infoDialog = new ReplicaInfoDialog(arg0.getContext());
				infoDialog.setTitle(item.getDisplayName());
				infoDialog.setIcon(item.getStoreImageUrl());
				if (!storeItem.isAvailable()) {
					infoDialog.setButtonText("Purchase");
					infoDialog.setButtonListener(onPurchaseClicked);
				} else {
					infoDialog.setButtonText("Ok");
					infoDialog.setErrorMessage("[" + storeItem.notAvailableMessage + "]");
				}
				infoDialog.setDescripton(item.getDescription());
				infoDialog.show();
				selectedItem = storeItem;
			}
		}
	};

	private AdapterView.OnItemClickListener onHistoryItemClicked = new AdapterView.OnItemClickListener() {

		public void onItemClick(
				android.widget.AdapterView<?> arg0,
				View arg1,
				int arg2,
				long arg3) {
			Object o = arg1.getTag();
			// Toast.makeText(arg1.getContext(), "Item clicked", Toast.LENGTH_LONG).show();
			if (o instanceof HistoryItem) {
				HistoryItem historyItem = (HistoryItem) o;
				Item item = historyItem.getItem();
				infoDialog = new ReplicaInfoDialog(arg0.getContext());
				infoDialog.setTitle(item.getDisplayName());
				infoDialog.setIcon(item.getStoreImageUrl());
				infoDialog.setButtonText("Ok");
				infoDialog.setDescripton(item.getDescription());
				infoDialog.show();
			}

		};
	};

	public ReplicaStoreManager(LinearLayout itemsList, GridView historyList) {
		this.itemsList = itemsList;
		storeItems = new ArrayList<StoreItem>();
		historyItems = new ArrayList<HistoryItem>();
		this.historyList = historyList;
		adapter = new HistoryListAdapter();
		historyList.setAdapter(adapter);
		historyList.setOnItemClickListener(onHistoryItemClicked);
		List<Long> purchasedItems = GamesPlatformManager.getPurchasedItemIds();
		for (Category c : GamesPlatformManager.getCategories()) {
			CategoryRow categoryRow = new CategoryRow(itemsList.getContext());
			categoryRow.setText(c.getDisplayName());
			itemsList.addView(categoryRow);
			for (Item i : c.getItems()) {
				Log.e("Item: " + (i == null ? "is null" : i.getDisplayName()));
				if ((purchasedItems != null) && (purchasedItems.contains(i.getId()))) {
					addHistoryItem(i);
				} else {
					addStoreItem(i);
				}
			}
		}
	}

	public void addStoreItem(
			Item item) {
		addStoreItem(item, onStoreItemClicked);
	}

	public void addStoreItem(
			Item item,
			View.OnClickListener listener) {
		StoreItem listItem = new StoreItem();
		storeItems.add(listItem);
		listItem.setItem(item);
		listItem.setOnClickListener(listener);
	}

	public void addHistoryItem(
			Item item) {
		addHistoryItem(item, null);
	}

	public void addHistoryItem(
			Item item,
			View.OnClickListener listener) {
		HistoryItem historyItem = new HistoryItem();
		historyItems.add(historyItem);
		historyItem.addItem(item);
		if (listener != null) {
			historyItem.setOnClickListener(listener);
		}
	}

	public class StoreItem {
		private Item storeItem;
		private ViewGroup itemLayout;
		private Map<Currency, Double> prices;
		private CustomImageView iconView;
		private TextView nameView;
		private TextView descView;
		private AdvancedTextView priceView;
		private String priceLabel;
		private String notAvailableMessage = null;

		public boolean isAvailable() {
			return notAvailableMessage == null;
		}

		private StoreItem() {
			init();
		}

		private StoreItem(Item item) {
			init();
			setItem(item);
		}

		private void init() {
			LayoutInflater li = (LayoutInflater) itemsList.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (ViewGroup) li.inflate(R.layout.store_item, null);

			iconView = (CustomImageView) itemLayout.findViewById(R.id.itemIcon);
			nameView = (TextView) itemLayout.findViewById(R.id.itemName);
			descView = (TextView) itemLayout.findViewById(R.id.itemDescription);
			priceView = (AdvancedTextView) itemLayout.findViewById(R.id.itemPrice);

			priceLabel = itemsList.getContext().getResources().getString(R.string.store_item_price_label);
		}

		public void setItem(
				Item item) {
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

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			itemLayout.setLayoutParams(params);
			notAvailableMessage = PowerupManager.isAvailable(item);
		}

		public void setPrice(
				int index) {
			if (priceView == null) {
				Log.e("ReplicaStoreManager: Price view is null");
				return;
			}
			if (GamesPlatformManager.getCurrencies() == null) {
				Log.e("ReplicaStoreManager: No GamesPlatform currencies");
				return;
			}
			Text price = priceView.new Text();
			if (prices == null) {
				Log.e("ReplicaStoreManager: Item has no prices");
				price.setText("FREE");
			} else {
				String priceString = null;
				for (Entry<Currency, Double> e : prices.entrySet()) {

					Currency c = e.getKey();
					Double d = e.getValue();
					if (d == 0) {
						continue;
					}
					if (priceString == null) {
						priceString = d + " " + c.getDisplayName();
					} else {
						priceString += "\n" + d + " " + c.getDisplayName();
					}
				}
				price.setText(priceString == null ? "FREE" : priceString);
			}
			price.setColor(STORE_ITEM_PRICE_COLOR);
			price.setGravity(Gravity.LEFT);

			Text priceText = priceView.new Text();
			priceText.setText(priceLabel);
			priceText.setColor(STORE_ITEM_PRICE_LABEL_COLOR);
			priceText.setGravity(Gravity.RIGHT);
			priceView.addText(priceText);
			priceView.addText(price);
		}

		public void setOnClickListener(
				View.OnClickListener listener) {
			itemLayout.setOnClickListener(listener);
		}

		public Item getItem() {
			return storeItem;
		}

		public void release() {
			if (itemLayout != null) {
				ImageView iconView = (ImageView) itemLayout.findViewById(R.id.itemIcon);
				iconView.setBackgroundDrawable(null);
				itemLayout.setOnClickListener(null);
				itemLayout.removeAllViews();
				itemsList.removeView(itemLayout);
				itemLayout.setTag(null);
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

		public void addItem(
				Item item) {
			this.historyItem = item;
			if (item == null) {
				Log.e("StoreManager: History Item is null");
			}
			icon.setImageFromInternet(item.getStoreImageUrl());
			icon.setTag(this);
			adapter.add(icon);
		}

		public Item getItem() {
			return historyItem;
		}

		public void release() {
			if (icon != null) {
				icon.setImageDrawable(null);
				icon.setOnClickListener(null);
				icon.setTag(null);
				icon = null;
			}
		}

		public void setOnClickListener(
				View.OnClickListener listener) {
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

		if ((infoDialog != null) && (infoDialog.isShowing())) {
			infoDialog.dismiss();
		}
	}

	public class CategoryRow extends TextView {

		public CategoryRow(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public CategoryRow(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public CategoryRow(Context context) {
			super(context);
			init();
		}

		private void init() {
			setBackgroundColor(DEFAULT_CATEGORY_BACKGROUND_COLOR);
			setTextColor(DEFAULT_CATEGORY_TEXT_COLOR);
			setTypeface(Typeface.DEFAULT_BOLD);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			setLayoutParams(params);
			setPadding(5, 5, 5, 5);
		}

	}
}
