package com.w3i.torch.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<Category> categories = null;
	private static GamesPlatformManager instance = null;
	private boolean initialized = false;

	private List<Currency> currencies;

	private GamesPlatformManager(Context context) {
		instance = this;
		GamesPlatformSDK.createInstance(PUBLISHER_ID, APP_ID, REST_URL, null, context);
		downloadStoreTree();
	}

	public static void downloadStoreTree() {
		checkInstance();
		GamesPlatformSDK.getInstance().createSession(instance);
		GamesPlatformSDK.getInstance().getCurrencies(instance);
		GamesPlatformSDK.getInstance().getStore(STORE_ID, instance);
	}

	public static void initialize(
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

	public static void trackItemPurchase(
			Item item) {
		checkInstance();
		if (isInitialized()) {
			Map<Currency, Double> userBalance = new HashMap<Currency, Double>();
			if (getCurrencies() != null) {
				List<Currency> currencies = getCurrencies();
				for (Currency c : currencies) {
					if (FundsManager.PEARLS.equals(c.getDisplayName())) {
						userBalance.put(c, (double) FundsManager.getPearls());

					} else if (FundsManager.CRYSTALS.equals(c.getDisplayName())) {
						userBalance.put(c, (double) FundsManager.getCrystals());
					}
				}

			}
			GamesPlatformSDK.getInstance().trackItemPurchase(item.getId(), 1L, item.getItemPrice(instance.currencies), userBalance, instance);
		}
	}

	private static void checkInstance() throws IllegalStateException {
		if (instance == null) {
			throw new IllegalStateException("GamesPlatformManager is not initialized.");
		}
	}

	@Override
	public void getStoreTreeCompleted(
			boolean success,
			Throwable exception,
			List<Category> store) {
		if ((success) && (store.size() > 0)) {
			categories = store;
			ItemManager.setCategories(categories);
			SharedPreferenceManager.loadPurchasedItems();
			PowerupManager.reloadItems(ItemManager.getPurchasedItems());
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

		GamesPlatformSDK.destroyInstance();
		instance = null;
	}
}