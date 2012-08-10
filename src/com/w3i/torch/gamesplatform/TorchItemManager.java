package com.w3i.torch.gamesplatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class TorchItemManager {
	private static TorchItemManager instance;
	private TorchItemCollection itemCollection;
	public static final String PREF_KEY = "ItemsCollection";

	private TorchItemManager() {
		itemCollection = new TorchItemCollection();
	}

	private static void checkInstance() {
		if (instance == null) {
			instance = new TorchItemManager();
		}
	}

	public static void readCategories(
			List<Category> categories) {
		checkInstance();
		TorchItemCollection tempCollection = new TorchItemCollection();
		for (Category category : categories) {
			for (Item item : category.getItems()) {
				TorchItem torchItem = new TorchItem(category, item);
				TorchItem oldTorchItem = instance.itemCollection.get(item.getId());
				if (oldTorchItem != null) {
					torchItem.setPurchased(oldTorchItem.isPurchased());
				}
				tempCollection.put(item.getId(), torchItem);
			}
		}
		instance.itemCollection = tempCollection;
	}

	public static void purchaseItem(
			TorchItem item) {
		checkInstance();
		item.setPurchased(true);
	}

	public static void reloadPurchasedItems() {
		checkInstance();
		instance.itemCollection.reloadItemPowerups();
	}

	public static List<String> isItemAvailable(
			TorchItem item) {
		checkInstance();
		return instance._isItemAvailable(item);
	}

	public static List<String> isItemAvailable(
			long id) {
		checkInstance();
		TorchItem item = instance.itemCollection.get(id);
		return instance._isItemAvailable(item);
	}

	private List<String> _isItemAvailable(
			TorchItem item) {
		List<String> requirementsList = new ArrayList<String>();
		checkItemRequirements(item, requirementsList);
		checkItemPrice(item, requirementsList);
		return requirementsList.size() == 0 ? null : requirementsList;
	}

	private void checkItemPrice(
			TorchItem item,
			List<String> requirementsList) {
		TorchCurrencyCollection currencies = TorchCurrencyManager.getCurrencies();
		if (currencies == null) {
			return;
		}
		for (Entry<Long, TorchCurrency> entry : currencies.entrySet()) {
			TorchCurrency torchCurrency = entry.getValue();
			if (TorchCurrencyManager.getBalance(entry.getKey()) < item.getItemPrice(torchCurrency.getCurrency())) {
				requirementsList.add("Insufficient " + torchCurrency.getDisplayName());
			}
		}
	}

	private void checkItemRequirements(
			TorchItem item,
			List<String> requirementsList) {
		List<Long> requirements = item.getRequirements();
		if (requirements == null) {
			return;
		}
		for (Long requiredId : requirements) {
			TorchItem requirement = itemCollection.get(requiredId);
			if (!requirement.isPurchased()) {
				requirementsList.add("Requires " + requirement.getDisplayName());
			}
		}
	}

	public static void purchaseItem(
			long id) {
		checkInstance();
		TorchItem item = instance.itemCollection.get(id);
		item.setPurchased(true);
	}

	public static void storeToPreferences(
			SharedPreferences preferences) {
		if (instance == null) {
			return;
		}
		Editor editor = preferences.edit();
		String json = new Gson().toJson(instance.itemCollection);
		editor.putString(PREF_KEY, json);
		editor.commit();
	}

	public static void loadFromPreferences(
			SharedPreferences preferences) {
		checkInstance();
		try {
			String json = preferences.getString(PREF_KEY, null);
			instance.itemCollection = new Gson().fromJson(json, TorchItemCollection.class);
		} catch (Exception e) {
			Log.e("TorchItemManager: Exception caught while loading items from preferences", e);
		}
	}

	public static String getItemIconUrl(
			long id) {
		checkInstance();
		TorchItem item = instance.itemCollection.get(id);
		if (item != null) {
			return item.getIcon();
		}
		return null;
	}

}
