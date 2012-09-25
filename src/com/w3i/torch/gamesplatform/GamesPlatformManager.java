package com.w3i.torch.gamesplatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.w3i.gamesplatformsdk.GamesPLatformListenerAdapter;
import com.w3i.gamesplatformsdk.GamesPlatformSDK;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.enums.RootCategoryType;
import com.w3i.torch.achivements.Achievement.State;
import com.w3i.torch.achivements.Achievement.Type;
import com.w3i.torch.achivements.AchievementManager;

public class GamesPlatformManager extends GamesPLatformListenerAdapter {
	public static final String REST_URL = "gp.api.w3i.com/PublicServices/GamesPlatformApiRestV1.svc";
	public static final int APP_ID = 24;
	public static final int PUBLISHER_ID = 8;
	public static final int STORE_ID = RootCategoryType.MAIN.getValue();

	private static GamesPlatformManager instance = null;
	private boolean currenciesReceived = false;
	private boolean itemsReceived = false;
	private boolean requestsRunning = false;

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
		instance.requestsRunning = true;
		GamesPlatformSDK.getInstance().createSession(instance);
		GamesPlatformSDK.getInstance().getCurrencies(instance);
		GamesPlatformSDK.getInstance().getStore(STORE_ID, instance);
	}

	public static void onResume() {
		checkInstance();
		if ((!instance.currenciesReceived) || (!instance.itemsReceived)) {
			if (!instance.requestsRunning) {
				downloadStoreTree();
			}
		}
	}

	@Override
	public void getCurrenciesCompleted(
			boolean success,
			Throwable exception,
			List<Currency> currencies) {
		if (success) {
			if (currencies.size() > 0) {
				TorchCurrencyManager.setCurrencies(currencies);
				SharedPreferenceManager.storeTorchCurrencyManager();
			}
			currenciesReceived = true;
		}
	}

	public static void trackItemPurchase(
			TorchItem item) {
		checkInstance();
		if (isInitialized()) {
			Map<Currency, Double> userBalance = new HashMap<Currency, Double>();
			Map<Currency, Double> itemPrice = new HashMap<Currency, Double>();
			TorchCurrencyCollection collection = TorchCurrencyManager.getCurrencies();
			if (collection != null) {
				for (Entry<Long, TorchCurrency> currencies : collection.entrySet()) {
					TorchCurrency currency = currencies.getValue();
					if (currency.getCurrency() != null) {
						userBalance.put(currency.getCurrency(), currency.getBalanceDouble());
						itemPrice.put(currency.getCurrency(), item.getItemPrice(currency.getCurrency()));
					}
				}

			}
			item.setTracked(true);
			GamesPlatformSDK.getInstance().trackItemPurchase(item.getId(), 1L, itemPrice, userBalance, instance);
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
			onStoreTreeCompleted(store);
			AchievementManager.setAchievementState(Type.GADGETEER, State.SET_PROGRESS);
		}
		requestsRunning = false;
	}

	private void onStoreTreeCompleted(
			List<Category> categories) {
		TorchItemManager.readCategories(categories);
		itemsReceived = true;
	}

	public static boolean isInitialized() {
		return instance != null;
	}

	public static boolean areRequestsCompleted() {
		checkInstance();
		return instance.itemsReceived && instance.currenciesReceived;
	}

	public static boolean areRequestExecuting() {
		checkInstance();
		return instance.requestsRunning;
	}

	public static void release() {
		if (instance == null) {
			return;
		}
		GamesPlatformSDK.destroyInstance();
		instance = null;
	}
}