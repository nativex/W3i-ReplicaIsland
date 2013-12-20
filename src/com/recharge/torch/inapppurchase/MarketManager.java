package com.recharge.torch.inapppurchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyCharacterMap.UnavailableException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.recharge.torch.inapppurchase.MarketConstants.BILLING_RESPONSE_CODE;
import com.recharge.torch.inapppurchase.MarketConstants.PURCHASE_TYPE;
import com.recharge.torch.utils.PreferencesManager;

public class MarketManager {
	private static final MarketManager instance = new MarketManager();
	private String key;
	private Context appContext;
	private boolean logEnabled = true;
	private MarketService service = new MarketService();
	private SparseArray<MarketItem> itemsBeingPurchased = new SparseArray<MarketItem>();
	private OnMarketServiceInitialized onInitializedListener;
	private OnMarketPurchasesQueried onPurchasesQueried;
	private Activity activity;
	static final Handler handler = new Handler();
	private Map<String, InAppPurchaseData> purchases = null;

	public static final void initialize(
			Activity activity,
			String publicKey) {
		if (isInitialized()) {
			return;
		}
		if (TextUtils.isEmpty(publicKey)) {
			throw new IllegalArgumentException("Invalid key");
		}
		instance.key = publicKey;
		instance.appContext = activity.getApplicationContext();
		instance.activity = activity;
		instance.service.bind(activity);
	}

	public static final boolean isInitialized() {
		return ((instance.appContext != null) && (instance.service.isConnected()) && (!TextUtils.isEmpty(instance.key)));
	}

	public static final void setOnInitializeListener(
			OnMarketServiceInitialized listener) {
		instance.onInitializedListener = listener;
	}

	public static final void setOnQueryPurchasesListener(
			OnMarketPurchasesQueried listener) {
		instance.onPurchasesQueried = listener;
	}

	static void setInitialized(
			final boolean success) {
		handler.post(new Runnable() {
			public void run() {
				if (instance.onInitializedListener != null) {
					instance.onInitializedListener.onInitialized(success);
				}
			}
		});
	}

	public static final boolean isPurchaseTypeSupported(
			PURCHASE_TYPE type) {
		return instance.service.isBillingSupported(type);
	}

	static final Context getContext() {
		return instance.appContext;
	}

	static final void logError(
			String msg,
			Throwable e) {
		if (instance.logEnabled) {
			Log.e(MarketConstants.LOG_TAG, msg, e);
		}
	}

	public static String getPublicKey() {
		return instance.key;
	}

	static final void registerItemForPurchase(
			MarketItem item) {
		instance.itemsBeingPurchased.append(item.getRequestCode(), item);
	}

	public static final void purchaseItem(
			MarketItem item) {
		if (isInitialized()) {
			if (instance.service.isBillingSupported(item.getPurchaseType())) {
				instance.service.purchaseItem(instance.activity, item);
			} else {
				item.onFailed(BILLING_RESPONSE_CODE.RESULT_BILLING_UNAVAILABLE);
			}
		}
	}

	public static final void handleActivityResult(
			int requestCode,
			int resultCode,
			Intent data) {
		if (isInitialized()) {
			MarketItem item = instance.itemsBeingPurchased.get(requestCode);
			if (item != null) {
				instance.service.handleActivityResult(item, resultCode, data);
				instance.itemsBeingPurchased.remove(requestCode);
			}
		}
	}

	public static final void consumePurchases() {
		if (isInitialized()) {
			Map<String, InAppPurchaseData> purchases = getPurchases();
			if (purchases.size() > 0) {
				List<MarketItem> unconsumed = new ArrayList<MarketItem>();
				for (InAppPurchaseData purchase : purchases.values()) {
					MarketItems itemType = MarketItems.findItemByMarketId(purchase.getProductId());
					if (itemType != MarketItems.UNAVAILABLE) {
						MarketItem item = itemType.getItemInstance();
						item.setPurchase(purchase);
						unconsumed.add(item);
					}
				}
				instance.service.consumeAsyncMultiple(unconsumed);
			}
		}
	}

	public static final void consumeItem(
			MarketItem item) {
		if (isInitialized()) {
			instance.service.consumePurchase(item);
		}
	}

	public static final void save(
			MarketItem item) {
		if (item.getPurchase() != null) {
			Map<String, InAppPurchaseData> purchases = getPurchases();
			purchases.put(item.getPurchase().getOrderId(), item.getPurchase());
			String json = new Gson().toJson(purchases);
			PreferencesManager.storePurchasedItems(json);
		}
	}

	static void setPurchasedItems(
			Map<String, InAppPurchaseData> purchases) {
		instance.purchases = purchases;
	}

	public static final Map<String, InAppPurchaseData> getPurchases() {
		if (instance.purchases == null) {
			try {
				String json = PreferencesManager.getPurchasedItems();
				instance.purchases = new Gson().fromJson(json, (new TypeToken<HashMap<String, InAppPurchaseData>>() {
				}).getType());
			} catch (Exception e) {
				logError("Failed to load purchased items", e);
				instance.purchases = new HashMap<String, InAppPurchaseData>(0);
			}
		}
		return instance.purchases;
	}

	static void fireOnQueryPurchasesCompleted(
			boolean success) {
		if (instance.onPurchasesQueried != null) {
			instance.onPurchasesQueried.onPurchasesQueried(success);
		}
	}

	public static final void dispose() {
		instance.service.dispose(instance.activity);
		instance.itemsBeingPurchased.clear();
		instance.activity = null;
	}
}
