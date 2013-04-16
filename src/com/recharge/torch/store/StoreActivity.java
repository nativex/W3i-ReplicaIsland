package com.recharge.torch.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.nativex.advertiser.NetworkConnectionManager;
import com.nativex.monetization.custom.views.CustomImageView;
import com.recharge.torch.R;
import com.recharge.torch.achivements.Achievement;
import com.recharge.torch.achivements.Achievement.State;
import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementListener;
import com.recharge.torch.achivements.AchievementManager;
import com.recharge.torch.activities.InAppPurchaseActivity;
import com.recharge.torch.activities.StartGameActivity;
import com.recharge.torch.gamesplatform.GamesPlatformManager;
import com.recharge.torch.gamesplatform.SharedPreferenceManager;
import com.recharge.torch.gamesplatform.TorchCurrency;
import com.recharge.torch.gamesplatform.TorchCurrencyManager;
import com.recharge.torch.gamesplatform.TorchCurrencyManager.OnCurrencyChanged;
import com.recharge.torch.gamesplatform.TorchItem;
import com.recharge.torch.gamesplatform.TorchItem.PurchaseState;
import com.recharge.torch.gamesplatform.TorchItemManager;
import com.recharge.torch.utils.MetrixUtils;
import com.recharge.torch.views.FundsView;
import com.recharge.torch.views.ReplicaInfoDialog;
import com.recharge.torch.views.ReplicaIslandToast;
import com.w3i.gamesplatformsdk.Log;

public class StoreActivity extends Activity {
	private LinearLayout storeList;
	private GridView historyList;
	private HistoryListAdapter adapter;
	private TorchItem selectedHistoryItem = null;
	private ViewFlinger flinger;
	private Map<TorchItem.PurchaseState, List<TorchItem>> items;
	private boolean itemsLoaded = false;
	private boolean buttonsLocked = false;

	private Map<Long, List<TorchItem>> categories;

	private AdapterView.OnItemClickListener onHistoryItemClicked = new AdapterView.OnItemClickListener() {

		public void onItemClick(
				android.widget.AdapterView<?> arg0,
				View historyItem,
				int arg2,
				long arg3) {
			Object tag = historyItem.getTag();
			if (tag instanceof TorchItem) {
				selectedHistoryItem = ((TorchItem) tag);
				showDialog(DIALOG_INFO_HISTORY);
			}
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

	public boolean onCreateOptionsMenu(
			android.view.Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.test_store_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.testMenuStoreAddBalance:
			for (Entry<Long, TorchCurrency> entry : TorchCurrencyManager.getCurrencies().entrySet()) {
				TorchCurrencyManager.addBalance(entry.getValue(), 1000);
			}
			return true;

		case R.id.testMenuStoreResetBalance:
			for (Entry<Long, TorchCurrency> entry : TorchCurrencyManager.getCurrencies().entrySet()) {
				TorchCurrencyManager.setBalance(entry.getValue(), 0);
			}

		case R.id.testMenuStoreResetItems:
			for (Entry<PurchaseState, List<TorchItem>> entry : TorchItemManager.getAllItems().entrySet()) {
				for (TorchItem item : entry.getValue()) {
					item.setPurchased(false);
				}
			}
			TorchItemManager.reloadPurchasedItems();
			storeList.removeAllViews();
			adapter.clear();
			loadItems();
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	public static final int DEFAULT_CATEGORY_BACKGROUND_COLOR = Color.BLACK;
	public static final int DEFAULT_CATEGORY_TEXT_COLOR = Color.WHITE;
	public static final int DEFAULT_PRICE_PEARLS_COLOR = Color.WHITE;
	public static final int DEFAULT_PRICE_CRYSTALS_COLOR = Color.RED;
	public static final String FONT_ITEM_NAME = "BEATSVIL.TTF";

	private static final int DIALOG_INFO_HISTORY = 233;
	private static final int DIALOG_INSUFFICIEN_CURRENCY = 4324;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_store);

		if (MetrixUtils.getDeviceScreenDiagInches(this) > 6) {
			View scroller = findViewById(R.id.storeFlinger);
			ViewGroup.LayoutParams params = scroller.getLayoutParams();
			params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600f, getResources().getDisplayMetrics());
		}

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

		// TorchCurrencyManager.setBalance(Currencies.PEARLS, 20000);
		// TorchCurrencyManager.setBalance(Currencies.CRYSTALS, 100);
		loadItems();
		AchievementManager.setAchievementState(Type.WINDOW_SHOPPER, State.START);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AchievementManager.registerAchievementListener(achievementListener);
		setFunds();
		FundsView.setOnCurrencyChangedListener(new OnCurrencyChanged() {

			@Override
			public void currencyChanged(
					TorchCurrency currency) {
				if (itemsLoaded) {
					resetItemAvailability(null);
				}
			}
		});
		resetItemAvailability(null);
		buttonsLocked = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		FundsView.releaseFunds();
	}

	private void loadItems() {
		items = TorchItemManager.getAllItems();

		if (items != null) {
			loadStoreItems();
			loadHistoryItems();
		}
		itemsLoaded = true;
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
		ViewGroup priceList = (ViewGroup) view.findViewById(R.id.uiFundsList);
		for (Entry<Long, TorchCurrency> entry : TorchCurrencyManager.getCurrencies().entrySet()) {
			TorchCurrency currency = entry.getValue();
			Double itemPrice = item.getItemPrice(currency.getCurrency());
			addCurrencyBlock(priceList, currency, itemPrice);
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

		if ((NetworkConnectionManager.getInstance(this).isConnected()) && (currency.getIcon() != null)) {
			icon.setImageFromInternet(currency.getIcon());
		} else {
			icon.setImageResource(currency.getDrawableResource());
		}
		amount.setText(String.format("%1$,.0f", price));

		view.addView(fundsLayout);
	}

	private void setItemErrorMessages(
			TorchItem item,
			TextView view) {
		if (view == null) {
			return;
		}
		if (item == null) {
			view.setVisibility(View.GONE);
		}
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
	}

	private void setFunds() {
		try {
			ViewGroup fundsView = (ViewGroup) findViewById(R.id.ui_funds_view);
			FundsView.setFunds(this, fundsView);
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
			infoDialog.setIcon(selectedHistoryItem.getIcon());
			infoDialog.setDescripton(selectedHistoryItem.getDescription());
			infoDialog.setButtonListener(onHistoryCloseClicked);
			infoDialog.setCloseListener(onHistoryCloseClicked);
			dialog = infoDialog;
			break;

		case DIALOG_INSUFFICIEN_CURRENCY:
			ReplicaInfoDialog insufficientCurrencyDialog = new ReplicaInfoDialog(this);
			insufficientCurrencyDialog.setTitle("Insufficient Currency");
			insufficientCurrencyDialog.setDescripton("Not enough currency. Please visit the offerwall for free currency");
			insufficientCurrencyDialog.setButtonText("Get Free Currency", 12f);
			insufficientCurrencyDialog.hideIcon();
			insufficientCurrencyDialog.setButtonListener(new View.OnClickListener() {

				@Override
				public void onClick(
						View v) {
					// PublisherManager.showIncentOfferWall();
					Intent intent = new Intent(v.getContext(), InAppPurchaseActivity.class);
					v.getContext().startActivity(intent);
					dismissDialog(DIALOG_INSUFFICIEN_CURRENCY);
				}
			});
			insufficientCurrencyDialog.setCloseListener(new View.OnClickListener() {

				@Override
				public void onClick(
						View v) {
					dismissDialog(DIALOG_INSUFFICIEN_CURRENCY);
				}
			});
			dialog = insufficientCurrencyDialog;
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
				if (purchaseItem(v, (TorchItem) tag)) {
					storeList.removeView(v);
				}
			}
		}
	};

	private boolean purchaseItem(
			View item,
			TorchItem storeItem) {
		if (TorchItemManager.canBePurchased(storeItem)) {
			TorchItemManager.purchaseItem(storeItem);
			TorchCurrencyManager.buyItem(storeItem);
			ReplicaIslandToast.makeStoreToast(this, storeItem);
			GamesPlatformManager.trackItemPurchase(storeItem);
			SharedPreferenceManager.storeTorchItemManager();
			SharedPreferenceManager.storeTorchCurrencyManager();
			updateAcheivementsOnPurchase(storeItem);
			resetItemAvailability(item);
			adapter.notifyDataSetChanged();
			return true;
		} else {
			if (!TorchItemManager.isAffordable(storeItem)) {
				showDialog(DIALOG_INSUFFICIEN_CURRENCY);
			}
			return false;
		}
	}

	private void updateAcheivementsOnPurchase(
			TorchItem item) {
		AchievementManager.setAchievementState(Type.HEALTH, State.UPDATE);
		AchievementManager.setAchievementState(Type.GADGETEER, State.UPDATE);
		AchievementManager.setAchievementState(Type.WINDOW_SHOPPER, State.FAIL);
	}

	private synchronized void resetItemAvailability(
			View item) {
		if (storeList != null) {
			for (int i = 0; i < storeList.getChildCount(); i++) {
				View child = storeList.getChildAt(i);
				resetItemAvailabilityInCategory(item, child);
			}
		}
	}

	private synchronized void resetItemAvailabilityInCategory(
			View item,
			View category) {
		if (category instanceof ViewGroup) {
			ViewGroup categoryGroup = (ViewGroup) category;
			for (int i = 0; i < categoryGroup.getChildCount(); i++) {
				View child = categoryGroup.getChildAt(i);
				if (child instanceof ViewGroup) {
					if (child == item) {
						categoryGroup.removeView(item);
					} else {
						TextView errorTextView = (TextView) child.findViewById(R.id.uiStoreItemErrorMessage);
						if (errorTextView != null) {
							setItemErrorMessages((TorchItem) child.getTag(), errorTextView);
						}
					}
				}
			}
			if (categoryGroup.getChildCount() < 2) {
				storeList.removeView(categoryGroup);
			}
		}
	}

	@Override
	public boolean onKeyDown(
			int keyCode,
			KeyEvent event) {
		boolean result = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (buttonsLocked) {
				return result;
			}
			View storeActivity = findViewById(R.id.ui_store_activity_container);
			Animation mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
			mFadeOutAnimation.setDuration(500);
			storeActivity.startAnimation(mFadeOutAnimation);
			mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(new Intent(this, StartGameActivity.class)));
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
	}

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {

			startActivity(mIntent);
			finish();

			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
			mIntent = null;
		}

		public void onAnimationRepeat(
				Animation animation) {

		}

		public void onAnimationStart(
				Animation animation) {

		}

	}

}
