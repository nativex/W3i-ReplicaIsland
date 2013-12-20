package com.recharge.torch.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.nativex.common.Log;
import com.nativex.monetization.custom.views.CustomImageView;
import com.recharge.torch.R;
import com.recharge.torch.achivements.Achievement;
import com.recharge.torch.achivements.Achievement.State;
import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementListener;
import com.recharge.torch.achivements.AchievementManager;
import com.recharge.torch.activities.InAppPurchaseActivity;
import com.recharge.torch.activities.StartGameActivity;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.Currency;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.OnCurrencyChanged;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.upgrades.TwinBatteriesUpgrade;
import com.recharge.torch.store.upgrades.Upgrades;
import com.recharge.torch.utils.MetrixUtils;
import com.recharge.torch.views.FundsView;
import com.recharge.torch.views.ReplicaInfoDialog;
import com.recharge.torch.views.ReplicaIslandToast;

public class StoreActivity extends Activity {
	private LinearLayout storeList;
	private GridView historyList;
	private HistoryListAdapter adapter;
	private Upgrade selectedHistoryItem = null;
	private ViewFlinger flinger;
	private boolean buttonsLocked = false;
	private Map<StoreCategory, ViewGroup> categories = new HashMap<StoreCategory, ViewGroup>();

	private AdapterView.OnItemClickListener onHistoryItemClicked = new AdapterView.OnItemClickListener() {

		public void onItemClick(
				android.widget.AdapterView<?> arg0,
				View historyItem,
				int arg2,
				long arg3) {
			Object tag = historyItem.getTag();
			if (tag instanceof Upgrade) {
				selectedHistoryItem = ((Upgrade) tag);
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
				for (Funds currency : Funds.values()) {
					currency.addAmount(1000);
				}
				return true;

			case R.id.testMenuStoreResetBalance:
				for (Funds currency : Funds.values()) {
					currency.setAmount(0);
				}
				return true;

			case R.id.testMenuStoreResetItems:
				for (Upgrades u : Upgrades.values()) {
					u.setOwned(false);
				}
				Upgrades.store();
				categories.clear();
				storeList.removeAllViews();
				adapter.clear();
				loadItems();
				return true;

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

		flinger = (ViewFlinger) findViewById(R.id.storeFlinger);

		if (MetrixUtils.getDeviceScreenDiagInches(this) > 6) {
			ViewGroup.LayoutParams params = flinger.getLayoutParams();
			params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600f, getResources().getDisplayMetrics());
		}

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

	OnCurrencyChanged onCurrencyChangedLister = new OnCurrencyChanged() {

		@Override
		public void currencyChanged(
				Funds currency) {
			resetItemAvailability(null);
			Log.d("Funds changed " + currency.name());
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		AchievementManager.registerAchievementListener(achievementListener);
		setFunds();
		Funds.registerListener(onCurrencyChangedLister);
		resetItemAvailability(null);
		buttonsLocked = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		FundsView.releaseFunds();
		Funds.removeListener(onCurrencyChangedLister);
	}

	private void loadItems() {
		loadStoreItems();
		loadHistoryItems();
	}

	private void addInCategory(
			Upgrades upgrade) {
		Upgrade item = upgrade.getUpgrade();
		ViewGroup categoryGroup = categories.get(item.getCategory());
		if (categoryGroup == null) {
			categoryGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_store_list_category, null);
			TextView categoryName = (TextView) categoryGroup.findViewById(R.id.uiStoreCategoryName);
			categoryName.setText(item.getCategory().getName());
			categoryName.setOnClickListener(onCategoryClicked);
			categoryName.setTag(categoryGroup);
			storeList.addView(categoryGroup);
			categories.put(item.getCategory(), categoryGroup);
		}
		categoryGroup.addView(createStoreItem(upgrade));
	}

	private void loadStoreItems() {
		for (Upgrades upgrade : Upgrades.values()) {
			if (!upgrade.isOwned()) {
				addInCategory(upgrade);
			}
		}
	}

	private View createStoreItem(
			Upgrades upgrade) {
		ViewGroup itemRow = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_store_item, null);
		CustomImageView itemIcon = (CustomImageView) itemRow.findViewById(R.id.uiStoreItemIcon);
		TextView itemName = (TextView) itemRow.findViewById(R.id.uiStoreItemName);
		TextView itemDescription = (TextView) itemRow.findViewById(R.id.uiStoreItemDescription);
		TextView itemErrorMessage = (TextView) itemRow.findViewById(R.id.uiStoreItemErrorMessage);
		ViewGroup fundsLayout = (ViewGroup) itemRow.findViewById(R.id.uiStoreItemLayoutFunds);
		Upgrade item = upgrade.getUpgrade();
		itemName.setText(item.getName());
		itemIcon.setImageResource(item.getIcon());
		itemDescription.setText(item.getDescription());
		setItemErrorMessages(upgrade, itemErrorMessage);
		setItemPrice(item, fundsLayout);
		itemRow.setOnClickListener(onStoreItemClickListener);
		itemRow.setTag(upgrade);
		return itemRow;
	}

	private void setItemPrice(
			Upgrade item,
			ViewGroup view) {
		ViewGroup priceList = (ViewGroup) view.findViewById(R.id.uiFundsList);
		for (Currency currency : item.getPrice()) {
			addCurrencyBlock(priceList, currency);
		}
	}

	private void addCurrencyBlock(
			ViewGroup view,
			Currency currency) {
		if (currency == null) {
			return;
		}
		ViewGroup fundsLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_funds_item, null);
		CustomImageView icon = (CustomImageView) fundsLayout.findViewById(R.id.uiFundsItemImage);
		TextView amount = (TextView) fundsLayout.findViewById(R.id.uiFundsItemAmount);
		if (currency.getIcon() > 0) {
			icon.setImageResource(currency.getIcon());
		}
		amount.setText(Long.toString(currency.getAmount()));
		view.addView(fundsLayout);
	}

	private void setItemErrorMessages(
			Upgrades item,
			TextView view) {
		if (view == null) {
			return;
		}
		List<String> errorMessages = checkItemErrorMessages(item.getUpgrade());
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

	private List<String> checkItemErrorMessages(
			Upgrade item) {
		List<String> errorMessages = new ArrayList<String>();
		String requirements = checkItemRequirements(item);
		if (requirements != null) {
			errorMessages.add(requirements);
		}
		String insufficientFunds = checkItemPrice(item);
		if (insufficientFunds != null) {
			errorMessages.add(insufficientFunds);
		}
		return errorMessages;
	}

	private String checkItemRequirements(
			Upgrade item) {
		List<String> requirements = new ArrayList<String>();
		for (Upgrades requirement : item.getRequirements()) {
			if (!requirement.isOwned()) {
				requirements.add(getResources().getString(requirement.getUpgrade().getName()));
			}
		}
		if (requirements.size() > 1) {
			String requirementString = null;
			for (int i = 0; i < requirements.size() - 1; i++) {
				if (i == 0) {
					requirementString = "Requires " + requirements.get(i);
				} else {
					requirementString += ", " + requirements.get(i);
				}
			}
			requirementString += " and " + requirements.get(requirements.size() - 1);
			return requirementString;
		} else if (requirements.size() == 1) {
			return "Requires " + requirements.get(0) + ".";
		}
		return null;
	}

	private List<Currency> insufficientFunds = new ArrayList<Currency>();

	private String checkItemPrice(
			Upgrade item) {
		if (item instanceof TwinBatteriesUpgrade) {
			toString();
		}
		insufficientFunds.clear();
		for (Currency c : item.getPrice()) {
			if (c.getAmount() > c.getId().getAmount()) {
				insufficientFunds.add(c);
			}
		}
		if (insufficientFunds.size() > 1) {
			String requires = null;
			for (int i = 0; i < insufficientFunds.size() - 1; i++) {
				Currency currency = insufficientFunds.get(i);
				if (i == 0) {
					requires = "Insufficient " + (currency.getAmount() - currency.getId().getAmount()) + " " + getResources().getString(currency.getName());
				} else {
					requires += ", " + (currency.getAmount() - currency.getId().getAmount()) + " " + getResources().getString(currency.getName());
				}
			}
			Currency currency = insufficientFunds.get(insufficientFunds.size() - 1);
			requires += " and " + (currency.getAmount() - currency.getId().getAmount()) + " " + getResources().getString(currency.getName()) + ".";
			return requires;
		} else if (insufficientFunds.size() == 1) {
			Currency currency = insufficientFunds.get(0);
			return "Insufficient " + (currency.getAmount() - currency.getId().getAmount()) + " " + getResources().getString(currency.getName()) + ".";
		}
		return null;
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
				infoDialog.setTitle(selectedHistoryItem.getName());
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
			if (tag instanceof Upgrades) {
				if (purchaseItem(v, (Upgrades) tag)) {
					storeList.removeView(v);
					adapter.reset();
				}
			}
		}
	};

	private boolean purchaseItem(
			View item,
			Upgrades storeItem) {
		if (storeItem.getUpgrade().hasMetRequirements()) {
			if (storeItem.getUpgrade().isAffordable()) {
				storeItem.buy();
				ReplicaIslandToast.makeStoreToast(this, storeItem);
				updateAcheivementsOnPurchase();
				resetItemAvailability(item);
				adapter.notifyDataSetChanged();
				return true;
			} else {
				if (!storeItem.getUpgrade().isAffordable()) {
					showDialog(DIALOG_INSUFFICIEN_CURRENCY);
				}
			}
		}
		return false;
	}

	private void updateAcheivementsOnPurchase() {
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
							setItemErrorMessages((Upgrades) child.getTag(), errorTextView);
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
