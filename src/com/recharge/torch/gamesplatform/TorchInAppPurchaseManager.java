package com.recharge.torch.gamesplatform;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nativex.common.Log;
import com.recharge.torch.activities.InAppPurchaseActivity;
import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.AttributeCurrency;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class TorchInAppPurchaseManager {
	private static TorchInAppPurchaseManager instance;
	private List<Category> items;
	private boolean enabled = false;
	public static Context context;

	private static void checkInstance() {
		if (instance == null) {
			instance = new TorchInAppPurchaseManager();
		}
	}

	public static void setEnabled(
			boolean isEnabled) {
		checkInstance();
		instance.enabled = isEnabled;
	}

	public static boolean isEnabled() {
		if (instance == null) {
			return false;
		}
		return ((instance.enabled) && (instance.items != null));
	}

	public static void initialize(
			List<Category> items) {
		checkInstance();
		instance.items = items;
		Intent intent = new Intent(context, InAppPurchaseActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static List<Category> getCategories() {
		checkInstance();
		return instance.items;
	}

	public static void buyItem(
			Activity activity,
			Item item) {
		checkInstance();
		if (instance.enabled) {
			GamesPlatformManager.trackInAppPurchase(activity, item);
		} else {
			Log.e("TorchInAppPurchaseManager: InAppPurchase disabled");
		}
	}

	public static void itemPurchased(
			Item item) {
		for (Attribute attribute : item.getAttributes()) {
			if (attribute.getType() == 2) {
				for (AttributeCurrency currency : attribute.getCurrencies()) {
					TorchCurrencyManager.addBalance(currency.getId().intValue(), currency.getValue().intValue());
				}
			}
		}
	}

	public static void release() {
		if (instance != null) {
			instance = null;
		}
	}
}
