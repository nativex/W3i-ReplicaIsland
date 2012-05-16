package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.w3i.gamesplatformsdk.Log;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.AdvancedTextView;
import com.w3i.offerwall.custom.views.AdvancedTextView.Text;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.store.VerticalTextView.ORIENTATION;

public class StoreActivity extends Activity {
	private LinearLayout itemsList;
	private GridView historyList;
	private HistoryListAdapter adapter;
	private ArrayList<BaseStoreItem> storeItems;
	private ArrayList<BaseStoreItem> historyItems;
	private BaseStoreItem selectedItem = null;

	private static final int STORE_ITEM_PRICE_LABEL_COLOR = Color.GRAY;
	private static final int STORE_ITEM_PRICE_COLOR = Color.argb(255, 255, 229, 34);
	public static final int DEFAULT_CATEGORY_BACKGROUND_COLOR = Color.DKGRAY;
	public static final int DEFAULT_CATEGORY_TEXT_COLOR = Color.WHITE;

	private static final int DIALOG_INFO_STORE = 232;
	private static final int DIALOG_INFO_HISTORY = 233;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_layout);

		itemsList = (LinearLayout) findViewById(R.id.storeItemsList);
		historyList = (GridView) findViewById(R.id.historyItems);
		if (itemsList == null) {
			Log.e("StoreActivity: cannot find Items list");
		}
		if (historyList == null) {
			Log.e("StoreActivity: cannot find History List");
		}
		adapter = new HistoryListAdapter();
		historyList.setAdapter(adapter);
		historyList.setOnItemClickListener(onHistoryItemClicked);

		TextView pearls = (TextView) findViewById(R.id.fundsPearlsQuantity);
		TextView crystals = (TextView) findViewById(R.id.fundsCrystalQuantity);
		pearls.setTextColor(Color.WHITE);
		crystals.setTextColor(Color.RED);

		setupScrollerTextView();
		loadItems();
		setFunds();
	}

	private void loadItems() {
		Map<String, List<Item>> storeItems = ItemManager.getStoreItems();
		if (storeItems != null) {
			for (Entry<String, List<Item>> e : storeItems.entrySet()) {
				if ((e.getValue() != null) && (e.getValue().size() > 0)) {
					CategoryRow catRow = new CategoryRow(this);
					catRow.setText(e.getKey());
					itemsList.addView(catRow);
					for (Item i : e.getValue()) {
						addStoreItem(i, e.getKey());
					}
				}
			}
		}

		Map<String, List<Item>> historyItems = ItemManager.getPurchasedItems();
		if (historyItems != null) {
			for (Entry<String, List<Item>> e : historyItems.entrySet()) {
				for (Item i : e.getValue()) {
					addHistoryItem(i, e.getKey());
				}
			}
		}
	}

	private void setupScrollerTextView() {
		VerticalTextView v = (VerticalTextView) findViewById(R.id.storeScrollHistory);
		v.setOrientation(ORIENTATION.LEFT);
		v.setText("Store");

		v = (VerticalTextView) findViewById(R.id.storeScrollStore);
		v.setOrientation(ORIENTATION.RIGHT);
		v.setText("Purchased Items");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		release();
		super.onDestroy();
	}

	private void setFunds() {
		try {
			TextView pearls = (TextView) findViewById(R.id.fundsPearlsQuantity);
			TextView crystals = (TextView) findViewById(R.id.fundsCrystalQuantity);

			pearls.setText(FundsManager.getPearls().toString());
			crystals.setText(FundsManager.getCrystals().toString());
		} catch (Exception e) {
			android.util.Log.e("ReplicaIsland", "StoreActivity: Unexpected exception caught while writing the resources.", e);
		}
	}

	@Override
	protected Dialog onCreateDialog(
			int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_INFO_HISTORY:
			if (selectedItem == null) {
				break;
			}
			if (selectedItem instanceof HistoryItem) {
				ReplicaInfoDialog infoDialog = new ReplicaInfoDialog(this);
				infoDialog.setTitle(selectedItem.getItem().getDisplayName());
				infoDialog.setButtonText("Ok");
				infoDialog.setIcon(selectedItem.getItem().getStoreImageUrl());
				infoDialog.setDescripton(selectedItem.getItem().getDescription());
				infoDialog.setButtonListener(onHistoryCloseClicked);
				infoDialog.setCloseListener(onHistoryCloseClicked);
				dialog = infoDialog;
			}
			break;
		case DIALOG_INFO_STORE:
			if (selectedItem == null) {
				break;
			}
			if (selectedItem instanceof StoreItem) {
				ReplicaInfoDialog infoDialog = new ReplicaInfoDialog(this);
				infoDialog.setTitle(selectedItem.getItem().getDisplayName());
				infoDialog.setIcon(selectedItem.getItem().getStoreImageUrl());
				infoDialog.setDescripton(selectedItem.getItem().getDescription());
				String errorMsg = ItemManager.isAvailable(selectedItem.getItem());
				if (errorMsg == null) {
					infoDialog.setButtonText("Purchase");
					infoDialog.setButtonListener(onPurchaseClicked);
				} else {
					infoDialog.setButtonText("Ok");
					infoDialog.setErrorMessage("[" + errorMsg + "]");
					infoDialog.setButtonListener(onStoreCloseClicked);
				}
				infoDialog.setCloseListener(onStoreCloseClicked);
				dialog = infoDialog;
			}
		}
		return dialog;
	}

	private void addHistoryItem(
			Item item,
			String category) {
		HistoryItem history = new HistoryItem(item);
		if (historyItems == null) {
			historyItems = new ArrayList<BaseStoreItem>();
		}
		historyItems.add(history);
		history.setCategory(category);
	}

	private void addStoreItem(
			Item item,
			String category) {
		StoreItem storeItem = new StoreItem(item);
		if (storeItems == null) {
			storeItems = new ArrayList<BaseStoreItem>();
		}
		storeItems.add(storeItem);
		storeItem.setCategory(category);
	}

	private View.OnClickListener onStoreCloseClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View arg0) {
			removeDialog(DIALOG_INFO_STORE);
		}
	};

	private View.OnClickListener onHistoryCloseClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View arg0) {
			removeDialog(DIALOG_INFO_HISTORY);
		}
	};

	private View.OnClickListener onPurchaseClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View arg0) {
			if (selectedItem != null) {
				Item item = selectedItem.getItem();
				PowerupManager.handleItem(item);
				ItemManager.addPurchasedItem(item, selectedItem.getCategory());
				FundsManager.buyItem(item);
				addHistoryItem(item, selectedItem.getCategory());
				storeItems.remove(selectedItem);
				selectedItem.release();
				selectedItem = null;
				removeDialog(DIALOG_INFO_STORE);
				setFunds();
			}
		}
	};

	private View.OnClickListener onStoreItemClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View arg0) {
			Object o = arg0.getTag();
			if (o instanceof StoreItem) {
				selectedItem = (BaseStoreItem) o;
				showDialog(DIALOG_INFO_STORE);
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
			if (o instanceof HistoryItem) {
				selectedItem = (BaseStoreItem) o;
				showDialog(DIALOG_INFO_HISTORY);
			}

		};
	};

	public abstract class BaseStoreItem {
		private Item item;
		private String category;

		public String getCategory() {
			return category;
		}

		public void setCategory(
				String category) {
			this.category = category;
		}

		public Item getItem() {
			return item;
		}

		public void setItem(
				Item item) {
			this.item = item;
		}

		public abstract void release();

	}

	public class StoreItem extends BaseStoreItem {
		ViewGroup itemLayout;

		private StoreItem() {
			init();
		}

		private StoreItem(Item item) {
			init();
			setItem(item);
		}

		private void init() {

		}

		public void setItem(
				Item item) {
			super.setItem(item);
			if (item == null) {
				Log.e("ReplicaStoreManager: Store Item is null");
				return;
			}

			LayoutInflater li = (LayoutInflater) itemsList.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (ViewGroup) li.inflate(R.layout.store_item, null);

			CustomImageView iconView = (CustomImageView) itemLayout.findViewById(R.id.itemIcon);
			TextView nameView = (TextView) itemLayout.findViewById(R.id.itemName);
			TextView descView = (TextView) itemLayout.findViewById(R.id.itemDescription);
			AdvancedTextView priceView = (AdvancedTextView) itemLayout.findViewById(R.id.itemPrice);

			iconView.setImageFromInternet(item.getStoreImageUrl());
			nameView.setText(item.getDisplayName());
			descView.setText(item.getDescription());
			setPrice(priceView, item);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			itemLayout.setLayoutParams(params);
			itemLayout.setOnClickListener(onStoreItemClicked);

			itemLayout.setTag(this);
			itemsList.addView(itemLayout);
		}

		public void setPrice(
				AdvancedTextView priceView,
				Item item) {
			if (priceView == null) {
				Log.e("ReplicaStoreManager: Price view is null");
				return;
			}
			if (GamesPlatformManager.getCurrencies() == null) {
				Log.e("ReplicaStoreManager: No GamesPlatform currencies");
				return;
			}
			Text price = priceView.new Text();
			Map<Currency, Double> prices = item.getItemPrice(GamesPlatformManager.getCurrencies());
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

			String priceLabel = getResources().getString(R.string.store_item_price_label);
			Text priceText = priceView.new Text();
			priceText.setText(priceLabel);
			priceText.setColor(STORE_ITEM_PRICE_LABEL_COLOR);
			priceText.setGravity(Gravity.RIGHT);
			priceView.addText(priceText);
			priceView.addText(price);
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

	public class HistoryItem extends BaseStoreItem {
		private CustomImageView icon;

		public HistoryItem() {
			icon = new CustomImageView(historyList.getContext());
		}

		public HistoryItem(Item item) {
			this();
			addItem(item);
		}

		public void addItem(
				Item item) {
			setItem(item);
			if (item == null) {
				Log.e("StoreManager: History Item is null");
			}
			icon.setImageFromInternet(item.getStoreImageUrl());
			icon.setTag(this);
			adapter.add(icon);
		}

		public void release() {
			if (icon != null) {
				icon.setImageDrawable(null);
				icon.setOnClickListener(null);
				icon.setTag(null);
				icon = null;
			}
		}

	}

	public void release() {

		if (storeItems != null) {
			for (BaseStoreItem i : storeItems) {
				i.release();
			}
			storeItems.clear();
			storeItems = null;
		}
		if (historyItems != null) {
			for (BaseStoreItem i : historyItems) {
				i.release();
			}
			historyItems.clear();
			historyItems = null;
		}
		if (adapter != null) {
			adapter.release();
			adapter = null;
		}
		if (itemsList != null) {
			itemsList.removeAllViews();
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
