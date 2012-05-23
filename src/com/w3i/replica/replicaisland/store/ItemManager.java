package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class ItemManager {
	private static final String KILLING_SPREE = "Killing Spree";
	private static final String GARBAGE_COLLECTOR = "Garbage Collector";
	private static ItemManager instance;
	private List<Category> categories;
	private Map<String, List<Item>> purchasedItems;
	private Map<String, List<Item>> availableItems;
	private Map<Long, ItemInfo> itemsByIds;

	private class ItemInfo {
		private Item i;
		private boolean purchased = false;
		@SuppressWarnings("unused")
		private String category = null;

		public ItemInfo(Item item, boolean isPurchased) {
			i = item;
			purchased = isPurchased;
		}

		public ItemInfo(Item item, boolean isPurchased, String category) {
			this(item, isPurchased);
			this.category = category;
		}
	}

	private ItemManager() {
		instance = this;
	}

	public static void create() {
		new ItemManager();
	}

	private static void checkInstance() {
		if (instance == null) {
			new ItemManager();
		}
	}

	static void setCategories(
			List<Category> categories) {
		checkInstance();
		instance.categories = categories;
	}

	public static List<Category> getCategories() {
		checkInstance();
		return instance.categories;
	}

	static boolean addPurchasedItem(
			Item i,
			String category) {
		checkInstance();
		return instance._addPurchasedItem(i, category);
	}

	private boolean _addPurchasedItem(
			Item i,
			String category) {
		if (i == null) {
			return false;
		}
		List<Item> items;
		if (purchasedItems == null) {
			purchasedItems = new HashMap<String, List<Item>>();
			items = new ArrayList<Item>();
			purchasedItems.put(category, items);
		} else if (!purchasedItems.containsKey(category)) {
			items = new ArrayList<Item>();
			purchasedItems.put(category, items);
		} else {
			items = purchasedItems.get(category);
		}
		items.add(i);
		if (availableItems.containsKey(category)) {
			List<Item> itemsInCategory = availableItems.get(category);
			if (itemsInCategory == null) {
				availableItems.remove(category);
				return true;
			}
			itemsInCategory.remove(i);
			if (itemsInCategory.size() <= 0) {
				availableItems.remove(category);
				return true;
			} else {
				return false;
			}
		}
		if (itemsByIds.containsKey(i.getId())) {
			itemsByIds.get(i.getId()).purchased = true;
		}
		if (i.getDisplayName().equals(GARBAGE_COLLECTOR)) {
			PowerupManager.setGarbageCollector(true);
		} else if (i.getDisplayName().equals(KILLING_SPREE)) {
			PowerupManager.setKillingSpreeEnabled(true);
		}
		return true;
	}

	static void setPurchasedItems(
			List<Long> purchasedItems) {
		checkInstance();
		instance._setPurchasedItems(purchasedItems);

	}

	private void _setPurchasedItems(
			List<Long> purchasedItemIds) {
		if (categories == null) {
			return;
		}
		if (availableItems == null) {
			availableItems = new HashMap<String, List<Item>>();
		}
		if (purchasedItems == null) {
			purchasedItems = new HashMap<String, List<Item>>();
		}
		if (itemsByIds == null) {
			itemsByIds = new HashMap<Long, ItemManager.ItemInfo>();
		}
		if ((purchasedItemIds != null) && (purchasedItemIds.size() > 0)) {
			for (Category c : categories) {
				for (Item i : c.getItems()) {
					Map<String, List<Item>> itemContainer;
					if (purchasedItemIds.contains(i.getId())) {
						itemContainer = purchasedItems;
						itemsByIds.put(i.getId(), new ItemInfo(i, true, c.getDisplayName()));
					} else {
						itemContainer = availableItems;
						itemsByIds.put(i.getId(), new ItemInfo(i, false, c.getDisplayName()));
					}
					List<Item> items = null;
					if (itemContainer.containsKey(c.getDisplayName())) {
						items = itemContainer.get(c.getDisplayName());
					} else {
						items = new ArrayList<Item>();
						itemContainer.put(c.getDisplayName(), items);
					}
					items.add(i);
				}
			}
		} else {
			for (Category c : categories) {
				for (Item i : c.getItems()) {
					List<Item> items = null;
					if (availableItems.containsKey(c.getDisplayName())) {
						items = availableItems.get(c.getDisplayName());
					} else {
						items = new ArrayList<Item>();
						availableItems.put(c.getDisplayName(), items);
						itemsByIds.put(i.getId(), new ItemInfo(i, false, c.getDisplayName()));
					}
					items.add(i);
				}
			}
		}
	}

	public static Map<String, List<Item>> getPurchasedItems() {
		checkInstance();
		return instance.purchasedItems;
	}

	static List<Long> getPurchasedItemsIds() {
		if (instance == null) {
			return null;
		}
		return instance._getPurchasedItemIds();

	}

	private List<Long> _getPurchasedItemIds() {
		if ((purchasedItems == null) || (purchasedItems.size() <= 0)) {
			return null;
		}
		List<Long> purchasedItemsIds = new ArrayList<Long>();
		for (Entry<String, List<Item>> e : purchasedItems.entrySet()) {
			if ((e.getValue() != null) && (e.getValue().size() > 0)) {
				for (Item i : e.getValue()) {
					purchasedItemsIds.add(i.getId());
				}
			}
		}
		return purchasedItemsIds;
	}

	public static Map<String, List<Item>> getStoreItems() {
		checkInstance();
		return instance.availableItems;
	}

	static Availability isAvailable(
			Item i) {
		checkInstance();
		return instance._isAvailable(i);
	}

	private Availability _isAvailable(
			Item i) {
		Availability available = new Availability();
		if (i == null) {
			available.setErrorMessage("Unavailable");
			return available;
		}
		for (Attribute a : i.getAttributes()) {
			String attributeName = a.getDisplayName();
			if (PowerupManager.UpgradeAttributes.REQUIREMENT.getAttributeName().equals(attributeName)) {
				long requiredItemId = Long.parseLong(a.getValue());
				if ((itemsByIds != null) && (itemsByIds.containsKey(requiredItemId))) {
					ItemInfo info = itemsByIds.get(requiredItemId);
					if (!info.purchased) {
						available.setErrorMessage("Requires " + info.i.getDisplayName());
						return available;
					}
				}
			}
		}
		List<Currency> currencies = GamesPlatformManager.getCurrencies();
		if (currencies == null) {
			available.setErrorMessage("Unavailable");
			return available;
		}
		Map<Currency, Double> prices = i.getItemPrice(currencies);
		for (Entry<Currency, Double> e : prices.entrySet()) {
			Currency c = e.getKey();
			if (c.getDisplayName().equals(FundsManager.PEARLS)) {
				if (FundsManager.getPearls() < e.getValue()) {
					available.setAvailable(true);
					available.setAffordable(false);
					available.setErrorMessage("Insufficient " + FundsManager.PEARLS);
					return available;
				}
			} else if (c.getDisplayName().equals(FundsManager.CRYSTALS)) {
				if (FundsManager.getCrystals() < e.getValue()) {
					available.setAvailable(true);
					available.setAffordable(false);
					available.setErrorMessage("Insufficient " + FundsManager.CRYSTALS);
					return available;
				}
			}
		}
		available.setAvailable(true);
		available.setAffordable(true);
		return available;
	}

	public static void release() {
		if (instance == null) {
			return;
		}
		instance._release();
	}

	private void _release() {
		if (purchasedItems != null) {
			purchasedItems.clear();
		}
		if (availableItems != null) {
			availableItems.clear();
		}
		if (categories != null) {
			categories.clear();
		}
		instance = null;
	}

	public class Availability {
		private boolean isAffordable = false;
		private boolean isAvailable = false;
		private String errorMessage = null;

		private Availability() {
		}

		public boolean isAffordable() {
			return isAffordable;
		}

		public boolean isAvailable() {
			return isAvailable;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		private void setAffordable(
				boolean isAffordable) {
			this.isAffordable = isAffordable;
		}

		private void setAvailable(
				boolean isAvailable) {
			this.isAvailable = isAvailable;
		}

		private void setErrorMessage(
				String errorMessage) {
			this.errorMessage = "[" + errorMessage + "]";
		}

	}
}
