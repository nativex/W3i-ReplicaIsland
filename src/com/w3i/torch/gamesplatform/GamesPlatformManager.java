package com.w3i.torch.gamesplatform;

import java.util.List;

import android.content.Context;

import com.w3i.gamesplatformsdk.GamesPLatformListenerAdapter;
import com.w3i.gamesplatformsdk.GamesPlatformSDK;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.enums.RootCategoryType;

public class GamesPlatformManager extends GamesPLatformListenerAdapter {
	public static final String REST_URL = "gp.api.w3i.com/PublicServices/GamesPlatformApiRestV1.svc";
	public static final int APP_ID = 24;
	public static final int PUBLISHER_ID = 8;
	public static final int STORE_ID = RootCategoryType.MAIN.getValue();

	private static GamesPlatformManager instance = null;
	private boolean currenciesReceived = false;
	private boolean itemsReceived = false;

	private GamesPlatformManager() {
	}

	public static void initializeManager(
			Context context) {
		instance = new GamesPlatformManager();
		GamesPlatformSDK.createInstance(PUBLISHER_ID, APP_ID, REST_URL, null, context);
		downloadStoreTree();
	}

	public static void downloadStoreTree() {
		checkInstance();
		GamesPlatformSDK.getInstance().createSession(instance);
		GamesPlatformSDK.getInstance().getCurrencies(instance);
		GamesPlatformSDK.getInstance().getStore(STORE_ID, instance);
	}

	@Override
	public void getCurrenciesCompleted(
			boolean success,
			Throwable exception,
			List<Currency> currencies) {
		if (success) {
			if (currencies.size() > 0) {
				TorchCurrencyManager.setCurrencies(currencies);
			}
			currenciesReceived = true;
		}
	}

	// public static void trackItemPurchase(
	// Item item) {
	// checkInstance();
	// if (isInitialized()) {
	// Map<Currency, Double> userBalance = new HashMap<Currency, Double>();
	// if (getCurrencies() != null) {
	// List<Currency> currencies = getCurrencies();
	// for (Currency c : currencies) {
	// if (FundsManager.PEARLS.equals(c.getDisplayName())) {
	// userBalance.put(c, (double) FundsManager.getPearls());
	//
	// } else if (FundsManager.CRYSTALS.equals(c.getDisplayName())) {
	// userBalance.put(c, (double) FundsManager.getCrystals());
	// }
	// }
	//
	// }
	// GamesPlatformSDK.getInstance().trackItemPurchase(item.getId(), 1L, item.getItemPrice(instance.currencies), userBalance, instance);
	// }
	// }

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
			onStoreTreeCompleted();
		}
	}

	private void onStoreTreeCompleted() {
		itemsReceived = true;
	}

	public static boolean isInitialized() {
		return instance != null;
	}

	public static boolean areRequestsCompleted() {
		checkInstance();
		return instance.itemsReceived && instance.currenciesReceived;
	}
}
