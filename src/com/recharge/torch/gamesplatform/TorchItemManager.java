package com.recharge.torch.gamesplatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.recharge.torch.achivements.Achievement.State;
import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementManager;
import com.recharge.torch.powerups.PowerupTypes;
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

	public static List<TorchItem> getItems(
			TorchItem.PurchaseState state) {
		if (instance == null) {
			return null;
		}
		return instance.itemCollection.getItems(state);
	}

	public static Map<TorchItem.PurchaseState, List<TorchItem>> getAllItems() {
		if (instance == null) {
			return null;
		}
		return instance.itemCollection.getAllItems();
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
		if (PowerupTypes.LIFE_POINTS.isEnabled()) {
			AchievementManager.setAchievementState(Type.HEALTH, State.UPDATE);
		}
		instance.itemCollection = tempCollection;
	}

	public static void purchaseItem(
			TorchItem item) {
		checkInstance();
		item.setPurchased(true);
		instance.itemCollection.reloadItemPowerups();
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

	private List<String> _isItemAffordable(
			TorchItem item) {
		List<String> requirementsList = new ArrayList<String>();
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
			Double itemPrice = item.getItemPrice(torchCurrency.getCurrency());
			if ((itemPrice != null) && (TorchCurrencyManager.getBalance(entry.getKey()) < itemPrice)) {
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
		instance.itemCollection.reloadItemPowerups();
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
		if (instance.itemCollection == null) {
			instance.itemCollection = new TorchItemCollection();
		} else {
			instance.itemCollection.reloadItemPowerups();
		}
		if (PowerupTypes.LIFE_POINTS.isEnabled()) {
			AchievementManager.setAchievementState(Type.HEALTH, State.UPDATE);
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

	public static boolean hasItems() {
		checkInstance();
		if ((instance.itemCollection != null) && (instance.itemCollection.size() > 0)) {
			return true;
		}
		return false;
	}

	public static boolean canBePurchased(
			TorchItem item) {
		checkInstance();
		List<String> errors = isItemAvailable(item);
		if ((errors == null) || (errors.size() == 0)) {
			return true;
		}
		return false;
	}

	public static boolean isAffordable(
			TorchItem item) {
		checkInstance();
		Object errors = instance._isItemAffordable(item);
		if (errors == null) {
			return true;
		}
		return false;
	}

	public static List<TorchItem> getItemsWithAttribute(
			PowerupTypes powerupType) {
		checkInstance();
		return instance.itemCollection.getItemsWithAttribute(powerupType);
	}

	public static void release() {
		if (instance == null) {
			return;
		}
		instance.itemCollection.clear();
		instance = null;
	}

}
