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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoclean.android.widget.flinger.ViewFlinger;
import com.w3i.gamesplatformsdk.Log;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.store.ItemManager.Availability;

public class StoreActivity extends Activity {
	private LinearLayout storeList;
	private GridView historyList;
	private HistoryListAdapter adapter;
	private List<CategoryBlock> categoryBlocks;
	private List<HistoryItem> historyItems;
	private Item selectedHistoryItem = null;
	private ViewFlinger flinger;

	private AdapterView.OnItemClickListener onHistoryItemClicked = new AdapterView.OnItemClickListener() {

		public void onItemClick(
				android.widget.AdapterView<?> arg0,
				View arg1,
				int arg2,
				long arg3) {
			Object tag = arg1.getTag();
			if (tag instanceof HistoryItem) {
				selectedHistoryItem = ((HistoryItem) tag).getItem();
				showDialog(DIALOG_INFO_HISTORY);
			}
		};
	};
	private View.OnClickListener onHistoryCloseClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			removeDialog(DIALOG_INFO_HISTORY);
		}
	};

	private View.OnClickListener onToStoreClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			if (flinger != null) {
				flinger.scrollLeft();
			}
		}
	};

	private View.OnClickListener onToHistoryClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			if (flinger != null) {
				flinger.scrollRight();
			}
		}
	};

	public static final int DEFAULT_CATEGORY_BACKGROUND_COLOR = Color.DKGRAY;
	public static final int DEFAULT_CATEGORY_TEXT_COLOR = Color.WHITE;
	public static final int DEFAULT_PRICE_PEARLS_COLOR = Color.WHITE;
	public static final int DEFAULT_PRICE_CRYSTALS_COLOR = Color.RED;
	public static final String FONT_ITEM_NAME = "BEATSVIL.TTF";

	private static final int DIALOG_INFO_HISTORY = 233;

	private Typeface fontItemName;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_layout);

		fontItemName = Typeface.createFromAsset(getAssets(), "fonts/" + FONT_ITEM_NAME);
		flinger = (ViewFlinger) findViewById(R.id.storeFlinger);
		View toStore = findViewById(R.id.historyToStoreImage);
		toStore.setOnClickListener(onToStoreClicked);
		View toHistory = findViewById(R.id.storeToHistoryImage);
		toHistory.setOnClickListener(onToHistoryClicked);
		storeList = (LinearLayout) findViewById(R.id.storeItemsList);
		historyList = (GridView) findViewById(R.id.historyItems);
		if (storeList == null) {
			Log.e("StoreActivity: cannot find Items list");
		}
		if (historyList == null) {
			Log.e("StoreActivity: cannot find History List");
		}
		adapter = new HistoryListAdapter();
		historyList.setAdapter(adapter);
		historyList.setOnItemClickListener(onHistoryItemClicked);

		TextView pearls = (TextView) findViewById(R.id.storeFundsPearlsQuantity);
		TextView crystals = (TextView) findViewById(R.id.storeFundsCrystalsQuantity);
		pearls.setTextColor(Color.WHITE);
		crystals.setTextColor(Color.RED);

		loadItems();
		setFunds();
	}

	private void loadItems() {
		loadStoreItems();
		loadHistoryItems();
	}

	private void loadStoreItems() {
		List<Category> categories = ItemManager.getCategories();
		for (Category c : categories) {
			List<Item> availableItems = getAvailableCategoryItems(c);
			if ((availableItems != null) && (availableItems.size() > 0)) {
				createCategory(availableItems, c);
			}
		}
	}

	private void loadHistoryItems() {
		List<Item> items = ItemManager.getPurchasedItems();
		if (items == null) {
			return;
		}
		for (Item itemInfo : items) {
			createHistoryItem(itemInfo);
		}
	}

	private void createHistoryItem(
			Item item) {
		HistoryItem historyItem = new HistoryItem(this);
		historyItem.setItem(item);
		if (historyItems == null) {
			historyItems = new ArrayList<StoreActivity.HistoryItem>();
		}
		historyItems.add(historyItem);
		adapter.add(historyItem);
	}

	private void createCategory(
			List<Item> items,
			Category category) {
		if (categoryBlocks == null) {
			categoryBlocks = new ArrayList<StoreActivity.CategoryBlock>();
		}
		CategoryBlock block = new CategoryBlock(this);
		block.setCategory(category);
		block.addItems(items);
		categoryBlocks.add(block);

		storeList.addView(block.getCategoryBlock());
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

	private void release() {
		if (categoryBlocks != null) {
			for (CategoryBlock c : categoryBlocks) {
				c.release();
			}
		}
		if (historyItems != null) {
			for (HistoryItem item : historyItems) {
				item.release();
			}
			historyItems.clear();
		}
		historyList.setAdapter(null);
		adapter.release();
	}

	private void setFunds() {
		try {
			View fundsLayout = findViewById(R.id.storeFunds);
			TextView pearls = (TextView) fundsLayout.findViewById(R.id.storeFundsPearlsQuantity);
			TextView crystals = (TextView) fundsLayout.findViewById(R.id.storeFundsCrystalsQuantity);

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
			if (selectedHistoryItem == null) {
				break;
			}
			ReplicaInfoDialog infoDialog = new ReplicaInfoDialog(this);
			infoDialog.setTitle(selectedHistoryItem.getDisplayName());
			infoDialog.setIcon(selectedHistoryItem.getStoreImageUrl());
			infoDialog.setDescripton(selectedHistoryItem.getDescription());
			infoDialog.setButtonListener(onHistoryCloseClicked);
			infoDialog.setCloseListener(onHistoryCloseClicked);
			dialog = infoDialog;
			break;
		}
		return dialog;
	}

	private List<Item> getAvailableCategoryItems(
			Category category) {
		List<Item> items = category.getItems();
		if (items == null) {
			return null;
		}

		List<Item> storeItems = ItemManager.getStoreItems();
		if (storeItems == null) {
			return null;
		}

		List<Item> availableItems = new ArrayList<Item>();

		for (Item item : items) {
			if (storeItems.contains(item)) {
				availableItems.add(item);
			}
		}

		return availableItems;
	}

	private View.OnClickListener onStoreItemClickListener = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			Object tag = v.getTag();
			if (tag instanceof StoreItem) {
				purchaseItem((StoreItem) tag);
			}
		}
	};

	private void purchaseItem(
			StoreItem storeItem) {
		if (storeItem.canBePurchased()) {
			Item item = storeItem.getItem();
			FundsManager.buyItem(item);
			PowerupManager.handleItem(item);
			GamesPlatformManager.trackItemPurchase(item);
			ItemManager.addPurchasedItem(item);
			ReplicaIslandToast.makeStoreToast(this, item).show();
			CategoryBlock parent = storeItem.getParent();
			SharedPreferenceManager.storePurchasedItems();
			storeItem.removeItemFromCategory();
			if (parent.getItemsCount() <= 0) {
				storeList.removeView(parent.getCategoryBlock());
				parent.release();
				categoryBlocks.remove(parent);
			}
			resetItemAvailability();
			setFunds();
			createHistoryItem(item);
		}
	}

	private void resetItemAvailability() {
		for (CategoryBlock block : categoryBlocks) {
			block.resetItemAvailability();
		}
	}

	public class CategoryBlock {
		private ArrayList<StoreItem> items;
		private TextView categoryName;
		private Category category;
		private boolean collapsed = true;
		private LinearLayout categoryBlock;

		private View.OnClickListener onCategoryNameClick = new View.OnClickListener() {

			@Override
			public void onClick(
					View v) {
				if (collapsed) {
					hideItems();
				} else {
					showItems();
				}
			}
		};

		public void resetItemAvailability() {
			for (StoreItem item : items) {
				item.setAvailability();
			}
		}

		public CategoryBlock(Context context) {
			init(context);
		}

		private void init(
				Context context) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			categoryBlock = (LinearLayout) inflater.inflate(R.layout.ui_store_list_category, null);

			categoryName = (TextView) categoryBlock.findViewById(R.id.storeCategoryName);
			categoryName.setOnClickListener(onCategoryNameClick);

			items = new ArrayList<StoreActivity.StoreItem>();
		}

		public LinearLayout getCategoryBlock() {
			return categoryBlock;
		}

		public int getItemsCount() {
			return items.size();
		}

		public void setCategory(
				Category category) {
			this.category = category;
			categoryName.setText(this.category.getDisplayName());

		}

		public void addItems(
				List<Item> items) {
			for (Item itemInfo : items) {
				createStoreItem(itemInfo);
			}
		}

		private void removeItem(
				StoreItem item) {
			categoryBlock.removeView(item.getItemLayout());
			item.release();
			items.remove(item);
		}

		private void createStoreItem(
				Item item) {
			StoreItem storeItem = new StoreItem(item);
			ViewGroup itemLayout = storeItem.getItemLayout();

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			itemLayout.setLayoutParams(params);
			itemLayout.setOnClickListener(onStoreItemClickListener);
			storeItem.setParent(this);

			categoryBlock.addView(itemLayout);
			items.add(storeItem);
		}

		public void showItems() {
			for (StoreItem item : items) {
				item.setVisibility(View.VISIBLE);
			}
			collapsed = true;
		}

		public void hideItems() {
			for (StoreItem item : items) {
				item.setVisibility(View.GONE);
			}
			collapsed = false;
		}

		public void release() {
			for (StoreItem item : items) {
				item.release();
			}
			items.clear();
			categoryBlock.removeAllViews();
		}
	}

	public class StoreItem {
		private ViewGroup itemLayout;
		private Item item;
		private CategoryBlock parent;

		private StoreItem(Item item) {
			this.item = item;
			init();
			addItem(item);
		}

		public boolean canBePurchased() {
			Availability availability = ItemManager.isAvailable(item);
			if ((availability.isAffordable()) && (availability.isAvailable())) {
				return true;
			}
			return false;
		}

		private void init() {
			LayoutInflater inflater = (LayoutInflater) StoreActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (ViewGroup) inflater.inflate(R.layout.store_item, null);
			itemLayout.setTag(this);
		}

		public void setParent(
				CategoryBlock parent) {
			this.parent = parent;
		}

		public void removeItemFromCategory() {
			parent.removeItem(this);
		}

		public CategoryBlock getParent() {
			return parent;
		}

		public void addItem(
				Item item) {

			if (item == null) {
				return;
			}

			if (itemLayout == null) {
				return;
			}

			setItemIcon(item.getStoreImageUrl());
			setItemName(item.getDisplayName());
			setItemDescription(item.getDescription());
			setItemPrice(item.getItemPrice(GamesPlatformManager.getCurrencies()));
			setAvailability();
		}

		public void setAvailability() {
			Availability itemAvailability = ItemManager.isAvailable(item);
			setItemErrorMessage(itemAvailability.getErrorMessage());
		}

		public void setVisibility(
				int visibility) {
			itemLayout.setVisibility(visibility);
		}

		public void setOnClickListener(
				View.OnClickListener listener) {
			itemLayout.setOnClickListener(listener);
		}

		public Item getItem() {
			return item;
		}

		public ViewGroup getItemLayout() {
			return itemLayout;
		}

		private void setItemIcon(
				String url) {
			CustomImageView icon = (CustomImageView) itemLayout.findViewById(R.id.itemIcon);
			if (icon != null) {
				icon.setImageFromInternet(url);
			}
		}

		private void setItemName(
				String text) {
			TextView name = (TextView) itemLayout.findViewById(R.id.itemName);
			if (name != null) {
				name.setTypeface(fontItemName);
				name.setText(text);
			}
		}

		private void setItemErrorMessage(
				String text) {
			TextView error = (TextView) itemLayout.findViewById(R.id.itemErrorMessage);
			if (error != null) {
				if (text != null) {
					error.setVisibility(View.VISIBLE);
					error.setText(text);
				} else {
					error.setVisibility(View.GONE);
				}
			}
		}

		private void setItemDescription(
				String text) {
			TextView description = (TextView) itemLayout.findViewById(R.id.itemDescription);
			if (description != null) {
				description.setText(text);
			}
		}

		private void setItemPrice(
				Map<Currency, Double> prices) {
			TextView pearls = (TextView) itemLayout.findViewById(R.id.fundsPearlsQuantity);
			TextView crystals = (TextView) itemLayout.findViewById(R.id.fundsCrystalQuantity);

			for (Entry<Currency, Double> entry : prices.entrySet()) {
				if (FundsManager.PEARLS.equals(entry.getKey().getDisplayName())) {
					if (pearls != null) {
						pearls.setText(Integer.toString((int) (entry.getValue() + 0.5)));
						pearls.setTextColor(DEFAULT_PRICE_PEARLS_COLOR);
					}
				} else if (FundsManager.CRYSTALS.equals(entry.getKey().getDisplayName())) {
					if (crystals != null) {
						crystals.setText(Integer.toString((int) (entry.getValue() + 0.5)));
						crystals.setTextColor(DEFAULT_PRICE_CRYSTALS_COLOR);
					}
				}
			}
		}

		public void release() {
			CustomImageView icon = (CustomImageView) itemLayout.findViewById(R.id.itemIcon);
			if (icon != null) {
				icon.setImageBitmap(null);
			}
			setOnClickListener(null);
		}
	}

	public class HistoryItem extends CustomImageView {
		private Item item;

		public HistoryItem(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public HistoryItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public HistoryItem(Context context) {
			super(context);
			init();
		}

		private void init() {
			setPadding(5, 5, 5, 5);
			setTag(this);
		}

		public void setItem(
				Item item) {
			this.item = item;
			setImageFromInternet(item.getStoreImageUrl());
		}

		public void release() {
			setImageBitmap(null);
		}

		public Item getItem() {
			return item;
		}

	}

}
