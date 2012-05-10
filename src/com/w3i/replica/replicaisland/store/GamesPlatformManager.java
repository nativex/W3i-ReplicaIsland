package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.GamesPLatformListenerAdapter;
import com.w3i.gamesplatformsdk.GamesPlatformSDK;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.gamesplatformsdk.rest.entities.enums.RootCategoryType;
import com.w3i.replica.replicaisland.PreferenceConstants;

public class GamesPlatformManager extends GamesPLatformListenerAdapter {
	public static final String REST_URL = "gp.api.w3i.com/PublicServices/GamesPlatformApiRestV1.svc";

	public static final int APP_ID = 24;
	public static final int PUBLISHER_ID = 8;
	public static final int STORE_ID = RootCategoryType.MAIN.getValue();
	private List<Category> categories = null;
	private static GamesPlatformManager instance = null;
	private StorePurchasedItems purchasedItems;
	private SharedPreferences preferences;
	private boolean initialized = false;

	private static int selectedCurrency = 0;

	private List<Currency> currencies;

	private GamesPlatformManager(Context context) {
		GamesPlatformSDK.createInstance(PUBLISHER_ID, APP_ID, REST_URL, null, context);
		preferences = context.getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		instance = this;
		downloadStoreTree();
	}

	public static void downloadStoreTree() {
		checkInstance();
		GamesPlatformSDK.getInstance().createSession(instance);
		GamesPlatformSDK.getInstance().getCurrencies(instance);
		GamesPlatformSDK.getInstance().getStore(STORE_ID, instance);
	}

	public static void createInstance(
			Context context) {
		new GamesPlatformManager(context);
	}

	public static boolean isInitialized() {
		checkInstance();
		if (instance.initialized) {
			return true;
		}
		downloadStoreTree();
		return false;
	}

	@Override
	public void getCurrenciesCompleted(
			boolean success,
			Throwable exception,
			List<Currency> currencies) {
		if (success) {
			this.currencies = currencies;
		}
	}

	private static void checkInstance() throws IllegalStateException {
		if (instance == null) {
			throw new IllegalStateException("GamesPlatforManager.createInstance(Context) not called");
		}
	}

	@Override
	public void getStoreTreeCompleted(
			boolean success,
			Throwable exception,
			List<Category> store) {
		if ((success) && (store.size() > 0)) {
			categories = store;
			PowerupManager.reset();
			List<Item> items = getPurchasedItems();
			if (items != null) {
				for (Item i : items) {
					PowerupManager.handleItem(i);
				}
			}
			initialized = true;
		}
	}

	public static boolean storeTreeDownloaded() {
		checkInstance();
		return instance.categories != null;
	}

	public static List<Category> getCategories() {
		checkInstance();
		return instance.categories;
	}

	public static List<Currency> getCurrencies() {
		checkInstance();
		if (instance.currencies == null) {
			Log.e("GamesPlatformManager: Currencies is null");
			return null;
		}
		return instance.currencies;
	}

	public static Currency getSelectedCurrenct() {
		checkInstance();
		if ((instance.currencies == null) || (instance.currencies.size() <= 0)) {
			return null;
		}
		return instance.currencies.get(selectedCurrency);
	}

	public static ArrayList<Long> getPurchasedItemIds() {
		checkInstance();
		instance.readPurchasedItemIds();
		if (instance.purchasedItems != null) {
			return instance.purchasedItems.getItems();
		}
		return null;
	}

	public static ArrayList<Item> getPurchasedItems() {
		checkInstance();
		return instance.selectPurchasedItems();
	}

	private ArrayList<Item> selectPurchasedItems() {
		if (purchasedItems == null) {
			readPurchasedItemIds();
		}
		if (purchasedItems == null) {
			return null;
		}
		if (categories == null) {
			return null;
		}
		ArrayList<Item> purchasedItemsList = new ArrayList<Item>();
		ArrayList<Long> purchasedItemIdsList = this.purchasedItems.getItems();
		if (purchasedItemIdsList == null) {
			return null;
		}
		for (Category c : categories) {
			if (c.getItems() != null) {
				for (Item i : c.getItems()) {
					if (purchasedItemIdsList.contains(i.getId())) {
						purchasedItemsList.add(i);
					}
				}
			}
		}
		return purchasedItemsList;
	}

	private void readPurchasedItemIds() {
		try {
			String json = preferences.getString(PreferenceConstants.PREFERENCE_PURCHASED_ITEMS, null);
			purchasedItems = new Gson().fromJson(json, StorePurchasedItems.class);
		} catch (Exception e) {
			Log.e("GamesPlatformManager: Unexpected exception caught while reading from shared preferences", e);
		}
	}

	public static void storePurchasedItem(
			Long id) {
		checkInstance();
		instance.storeItem(id);
	}

	private void storeItem(
			Long id) {
		try {
			if (purchasedItems == null) {
				purchasedItems = new StorePurchasedItems();
				purchasedItems.setItems(new ArrayList<Long>());
			}
			purchasedItems.getItems().add(id);
			SharedPreferences.Editor editor = preferences.edit();
			String json = new Gson().toJson(purchasedItems);
			editor.putString(PreferenceConstants.PREFERENCE_PURCHASED_ITEMS, json);
			editor.commit();
		} catch (Exception e) {
			Log.e("GamesPlatformManager: Unexpected exception caught while writing to shared preferences", e);
		}
	}

	public static void release() {
		checkInstance();
		instance.releaseManager();
	}

	private void releaseManager() {
		if (categories != null) {
			categories.clear();
		}
		if (currencies != null) {
			currencies.clear();
		}
		if ((purchasedItems != null) && (purchasedItems.getItems() != null)) {
			purchasedItems.getItems().clear();
		}
		GamesPlatformSDK.destroyInstance();
		preferences = null;
		instance = null;
	}
}
