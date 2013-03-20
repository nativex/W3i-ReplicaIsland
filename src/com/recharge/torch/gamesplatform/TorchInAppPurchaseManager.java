package com.recharge.torch.gamesplatform;

import java.util.List;

import android.app.Activity;

import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.AttributeCurrency;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class TorchInAppPurchaseManager {
	private static TorchInAppPurchaseManager instance;
	private List<Category> items;

	private static void checkInstance() {
		if (instance == null) {
			instance = new TorchInAppPurchaseManager();
		}
	}

	public static void initialize(
			List<Category> items) {
		checkInstance();
		instance.items = items;
	}

	public static List<Category> getCategories() {
		checkInstance();
		return instance.items;
	}

	public static void buyItem(
			Activity activity,
			Item item) {
		GamesPlatformManager.trackInAppPurchase(activity, item);
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
