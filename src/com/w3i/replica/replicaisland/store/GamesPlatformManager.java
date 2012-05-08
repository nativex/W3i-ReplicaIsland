package com.w3i.replica.replicaisland.store;

import java.util.List;

import android.content.Context;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.GamesPLatformListenerAdapter;
import com.w3i.gamesplatformsdk.GamesPlatformSDK;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.gamesplatformsdk.rest.entities.enums.RootCategoryType;

public class GamesPlatformManager extends GamesPLatformListenerAdapter {
	public static final String REST_URL = "gp.api.w3i.com/PublicServices/GamesPlatformApiRestV1.svc";

	public static final int APP_ID = 24;
	public static final int PUBLISHER_ID = 8;
	public static final int STORE_ID = RootCategoryType.MAIN.getValue();
	private Category itemList = null;
	private static GamesPlatformManager instance = null;

	private static int selectedCurrency = 0;

	private List<Currency> currencies;

	private GamesPlatformManager(Context context) {
		GamesPlatformSDK.createInstance(PUBLISHER_ID, APP_ID, REST_URL, null,
				context);
		instance = this;
		downloadStoreTree();
	}

	public static void downloadStoreTree() {
		checkInstance();
		GamesPlatformSDK.getInstance().createSession(instance);
		GamesPlatformSDK.getInstance().getCurrencies(instance);
		GamesPlatformSDK.getInstance().getStore(STORE_ID, instance);
	}

	public static void createInstance(Context context) {
		new GamesPlatformManager(context);
	}

	@Override
	public void getCurrenciesCompleted(boolean success, Throwable exception,
			List<Currency> currencies) {
		if (success) {
			this.currencies = currencies;
		}
	}

	private static void checkInstance() throws IllegalStateException {
		if (instance == null) {
			throw new IllegalStateException(
					"GamesPlatforManager.createInstance(Context) not called");
		}
	}

	@Override
	public void getStoreTreeCompleted(boolean success, Throwable exception,
			List<Category> store) {
		if ((success) && (store.size() > 0)) {
			itemList = store.get(0);
		}
	}

	public static boolean storeTreeDownloaded() {
		checkInstance();
		return instance.itemList != null;
	}

	public static List<Item> getItems() {
		checkInstance();
		return instance.itemList.getItems();
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
}
