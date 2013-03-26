package com.recharge.torch.gamesplatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.recharge.torch.achivements.Achievement.State;
import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementManager;
import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.GamesPLatformListenerAdapter;
import com.w3i.gamesplatformsdk.GamesPlatformSDK;
import com.w3i.gamesplatformsdk.MarketManager;
import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.gamesplatformsdk.rest.entities.enums.RootCategoryType;

public class GamesPlatformManager extends GamesPLatformListenerAdapter {
	public static final String REST_URL = "gp.api.w3i.com/PublicServices/GamesPlatformApiRestV1.svc";
	public static final int APP_ID = 24;
	public static final int PUBLISHER_ID = 8;

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
		GamesPlatformSDK.getInstance().getStore(RootCategoryType.CUSTOM.getValue(), instance);
		GamesPlatformSDK.getInstance().getStore(RootCategoryType.MAIN.getValue(), instance);
		GamesPlatformSDK.getInstance().getStore(RootCategoryType.IN_APP_PURCHASE.getValue(), instance);
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
			GamesPlatformSDK.getInstance().trackItemPurchase(item.getId(), 1L, instance);
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
			Category category = store.get(0);
			if (category.getRootType() == RootCategoryType.MAIN.getValue()) {
				onStoreTreeCompleted(store);
				AchievementManager.setAchievementState(Type.GADGETEER, State.SET_PROGRESS);
			} else if (category.getRootType() == RootCategoryType.CUSTOM.getValue()) {
				try {
					for (Item i : category.getItems()) {
						if (i.getDisplayName().equals("Settings")) {
							for (Attribute att : i.getCustomAttributes()) {
								if (att.getName().equals("Key")) {
									String key = att.getValue();
									if ((key != null) && (!key.trim().equals(""))) {
										MarketManager.destroyInstance();
										MarketManager.createInstance(key, "com.recharge.torch");
										TorchInAppPurchaseManager.setEnabled(true);
									}
									break;
								}
							}
							break;
						}
					}
				} catch (Exception e) {
					TorchInAppPurchaseManager.setEnabled(false);
					Log.e("GamesPlatformManager: Exception caught while reading Torch settings", e);
				}
			} else if (category.getRootType() == RootCategoryType.IN_APP_PURCHASE.getValue()) {
				TorchInAppPurchaseManager.initialize(store);
			}
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

	public static void trackInAppPurchase(
			final Activity activity,
			final Item item) {
		if (activity == null) {
			return;
		}
		GamesPlatformSDK.getInstance().buyItemFromMarket(activity, item.getId(), 1L, new GamesPLatformListenerAdapter() {
			@Override
			public void purchaseFromMarketCompleted(
					final boolean success,
					final Throwable exception) {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							Toast.makeText(activity, "Listener Fired", Toast.LENGTH_LONG).show();
							if (success) {
								Toast.makeText(activity, "Transaction successful", Toast.LENGTH_SHORT).show();
								TorchInAppPurchaseManager.itemPurchased(item);
							} else {
								Toast.makeText(activity, "Transaction failed" + (exception != null ? ": " + exception.getMessage() : "."), Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							Log.e("GamesPlatformManager: Exception caught in trackInAppPurchase() listener", e);
						}

					}
				});

			}
		});
	}
}
