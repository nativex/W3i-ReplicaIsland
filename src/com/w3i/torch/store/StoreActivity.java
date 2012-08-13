package com.w3i.torch.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoclean.android.widget.flinger.ViewFlinger;
import com.w3i.gamesplatformsdk.Log;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.torch.R;
import com.w3i.torch.achivements.Achievement;
import com.w3i.torch.achivements.Achievement.State;
import com.w3i.torch.achivements.Achievement.Type;
import com.w3i.torch.achivements.AchievementListener;
import com.w3i.torch.achivements.AchievementManager;
import com.w3i.torch.gamesplatform.TorchCurrency;
import com.w3i.torch.gamesplatform.TorchCurrencyManager;
import com.w3i.torch.gamesplatform.TorchItem;
import com.w3i.torch.gamesplatform.TorchItemManager;
import com.w3i.torch.views.ReplicaInfoDialog;
import com.w3i.torch.views.ReplicaIslandToast;

public class StoreActivity extends Activity {
	private LinearLayout storeList;
	private GridView historyList;
	private HistoryListAdapter adapter;
	private Item selectedHistoryItem = null;
	private ViewFlinger flinger;
	private int historyImageSize;
	private Map<TorchItem.PurchaseState, List<TorchItem>> items;

	private Map<Long, List<TorchItem>> categories;

	private AdapterView.OnItemClickListener onHistoryItemClicked = new AdapterView.OnItemClickListener() {

		public void onItemClick(
				android.widget.AdapterView<?> arg0,
				View arg1,
				int arg2,
				long arg3) {
			Object tag = arg1.getTag();
			// if (tag instanceof HistoryItem) {
			// selectedHistoryItem = ((HistoryItem) tag).getItem();
			// showDialog(DIALOG_INFO_HISTORY);
			// }
		};
	};
	private View.OnClickListener onHistoryCloseClicked = new View.OnClickListener() {

		public void onClick(
				View v) {
			removeDialog(DIALOG_INFO_HISTORY);
		}
	};

	private View.OnClickListener onToStoreClicked = new View.OnClickListener() {

		public void onClick(
				View v) {
			if (flinger != null) {
				flinger.scrollLeft();
			}
		}
	};

	private View.OnClickListener onToHistoryClicked = new View.OnClickListener() {

		public void onClick(
				View v) {
			if (flinger != null) {
				flinger.scrollRight();
			}
		}
	};

	private AchievementListener achievementListener = new AchievementListener() {

		@Override
		public void achievementUnlocked(
				final Achievement achievement) {
			final Activity context = StoreActivity.this;
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ReplicaIslandToast.makeAchievementUnlockedToast(context, achievement);

				}
			});
		}

		@Override
		public void achievementDone(
				final Achievement achievement) {
			final Activity context = StoreActivity.this;
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ReplicaIslandToast.makeAchievementDoneToast(context, achievement);

				}
			});

		}

		@Override
		public void achievementProgressUpdate(
				final Achievement achievement,
				final int percentDone) {
			final Activity context = StoreActivity.this;
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ReplicaIslandToast.makeAchievementProgressUpdateToast(context, achievement, percentDone);

				}
			});
		}
	};

	public static final int DEFAULT_CATEGORY_BACKGROUND_COLOR = Color.BLACK;
	public static final int DEFAULT_CATEGORY_TEXT_COLOR = Color.WHITE;
	public static final int DEFAULT_PRICE_PEARLS_COLOR = Color.WHITE;
	public static final int DEFAULT_PRICE_CRYSTALS_COLOR = Color.RED;
	public static final String FONT_ITEM_NAME = "BEATSVIL.TTF";

	private static final int DIALOG_INFO_HISTORY = 233;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_layout);

		historyImageSize = getResources().getInteger(R.integer.store_history_image_size_code);
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
		AchievementManager.setAchievementState(Type.WINDOW_SHOPPER, State.START);
	}

	@Override
	protected void onStart() {
		super.onStart();
		AchievementManager.registerAchievementListener(achievementListener);
	}

	private void loadItems() {
		items = TorchItemManager.getAllItems();

		if (items != null) {
			loadStoreItems();
			loadHistoryItems();
		}
	}

	private void createCategory(
			List<TorchItem> items) {
		if ((items != null) && (items.size() > 0)) {
			ViewGroup categoryGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_store_list_category, null);
			TextView categoryName = (TextView) categoryGroup.findViewById(R.id.uiStoreCategoryName);
			categoryName.setText(items.get(0).getCategoryName());
			categoryName.setOnClickListener(onCategoryClicked);
			categoryName.setTag(categoryGroup);
			Collections.sort(items, new Comparator<TorchItem>() {

				@Override
				public int compare(
						TorchItem object1,
						TorchItem object2) {
					if (object1.getId() < object2.getId()) {
						return -1;
					} else if (object1.getId() > object2.getId()) {
						return 1;
					}
					return 0;
				}

			});
			for (TorchItem item : items) {
				categoryGroup.addView(createStoreItem(item));
			}
			storeList.addView(categoryGroup);
		}
	}

	private void loadStoreItems() {
		List<TorchItem> availableItems = items.get(TorchItem.PurchaseState.AVAILABLE);
		categories = new HashMap<Long, List<TorchItem>>();
		if (availableItems != null) {
			for (TorchItem item : availableItems) {
				List<TorchItem> items = categories.get(item.getCategoryId());
				if (items == null) {
					items = new ArrayList<TorchItem>();
				}
				items.add(item);
				categories.put(item.getCategoryId(), items);
			}
		}
		for (Entry<Long, List<TorchItem>> entry : categories.entrySet()) {
			createCategory(entry.getValue());
		}
	}

	private View createStoreItem(
			TorchItem item) {

		ViewGroup itemRow = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_store_item, null);
		CustomImageView itemIcon = (CustomImageView) itemRow.findViewById(R.id.uiStoreItemIcon);
		TextView itemName = (TextView) itemRow.findViewById(R.id.uiStoreItemName);
		TextView itemDescription = (TextView) itemRow.findViewById(R.id.uiStoreItemDescription);
		TextView itemErrorMessage = (TextView) itemRow.findViewById(R.id.uiStoreItemErrorMessage);
		ViewGroup fundsLayout = (ViewGroup) itemRow.findViewById(R.id.uiStoreItemLayoutFunds);

		itemName.setText(item.getDisplayName());
		itemIcon.setImageFromInternet(item.getIcon());
		itemDescription.setText(item.getDescription());
		setItemErrorMessages(item, itemErrorMessage);
		setItemPrice(item, fundsLayout);
		itemRow.setOnClickListener(onStoreItemClickListener);
		itemRow.setTag(item);
		return itemRow;
	}

	private void setItemPrice(
			TorchItem item,
			ViewGroup view) {
		for (Entry<Long, TorchCurrency> entry : TorchCurrencyManager.getCurrencies().entrySet()) {
			TorchCurrency currency = entry.getValue();
			Double itemPrice = item.getItemPrice(currency.getCurrency());
			addCurrencyBlock(view, currency, itemPrice);
		}
	}

	private void addCurrencyBlock(
			ViewGroup view,
			TorchCurrency currency,
			Double price) {
		if (price == null) {
			return;
		}
		ViewGroup fundsLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_funds_item, null);
		CustomImageView icon = (CustomImageView) fundsLayout.findViewById(R.id.uiFundsItemImage);
		TextView amount = (TextView) fundsLayout.findViewById(R.id.uiFundsItemAmount);

		icon.setImageFromInternet(currency.getIcon());
		amount.setText(String.format("%1$,.0f", price));

		view.addView(fundsLayout);
	}

	private void setItemErrorMessages(
			TorchItem item,
			TextView view) {
		List<String> errorMessages = TorchItemManager.isItemAvailable(item);
		if ((errorMessages == null) || (errorMessages.size() == 0)) {
			view.setVisibility(View.GONE);
			return;
		}
		StringBuilder messages = new StringBuilder();
		for (int i = 0; i < errorMessages.size() - 1; i++) {
			messages.append(errorMessages.get(i) + "\n");
		}
		messages.append(errorMessages.get(errorMessages.size() - 1));
		view.setText(messages.toString());
		view.setVisibility(View.VISIBLE);
	}

	private void loadHistoryItems() {

	}

	@Override
	protected void onDestroy() {
		release();
		AchievementManager.setAchievementState(Type.WINDOW_SHOPPER, State.FINISH);
		AchievementManager.registerAchievementListener(null);
		super.onDestroy();
	}

	private void release() {
		historyList.setAdapter(null);
		adapter.release();
	}

	private void setFunds() {
		try {
			// TODO
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

	private View.OnClickListener onCategoryClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			onCategoryClicked(v);
		}
	};

	private void onCategoryClicked(
			View categoryName) {
		Object tag = categoryName.getTag();
		if (tag instanceof ViewGroup) {
			ViewGroup category = (ViewGroup) tag;
			for (int i = 0; i < category.getChildCount(); i++) {
				View child = category.getChildAt(i);
				if (child instanceof ViewGroup) {
					if (child.getVisibility() == View.VISIBLE) {
						setCategoryChildGone(child);
					} else {
						setCategoryChildVisible(child);
					}
				}
			}
		}
	}

	private void setCategoryChildGone(
			final View v) {
		if (v == null) {
			return;
		}
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.ui_store_activity_child_visible_animation);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(
					Animation animation) {
			}

			@Override
			public void onAnimationRepeat(
					Animation animation) {
			}

			@Override
			public void onAnimationEnd(
					Animation animation) {
				v.setVisibility(View.GONE);
			}
		});
		v.startAnimation(anim);
	}

	private void setCategoryChildVisible(
			View v) {
		if (v == null) {
			return;
		}
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.ui_store_activity_child_gone_animation);
		v.setVisibility(View.VISIBLE);
		v.startAnimation(anim);
	}

	private View.OnClickListener onStoreItemClickListener = new View.OnClickListener() {

		public void onClick(
				View v) {
			Object tag = v.getTag();
			if (tag instanceof TorchItem) {
				// purchaseItem((TorchItem) tag);
			}
		}
	};

	// private void purchaseItem(
	// StoreItem storeItem) {
	// if (storeItem.canBePurchased()) {
	// Item item = storeItem.getItem();
	// FundsManager.buyItem(item);
	// ReplicaIslandToast.makeStoreToast(this, item);
	// PowerupManager.handleItem(item);
	// GamesPlatformManager.trackItemPurchase(item);
	// ItemManager.addPurchasedItem(item);
	// CategoryBlock parent = storeItem.getParent();
	// SharedPreferenceManager.storePurchasedItems();
	// AchievementManager.setAchievementState(Type.WINDOW_SHOPPER, State.FAIL);
	// storeItem.removeItemFromCategory();
	// if (parent.getItemsCount() <= 0) {
	// storeList.removeView(parent.getCategoryBlock());
	// parent.release();
	// categoryBlocks.remove(parent);
	// }
	// resetItemAvailability();
	// setFunds();
	// }
	// }

}
