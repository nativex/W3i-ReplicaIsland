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
	private static final long GARBAGE_COLLECTOR_ID = 1049;
	private static final long KILLING_SPREE_ID = 1050;
	private static ItemManager instance;
	private List<Category> categories;
	private Map<Long, ItemInfo> itemsByIds;
	private List<ItemInfo> purchasedItems;
	private List<ItemInfo> availableItems;
	private boolean purchasedItemsLoaded = false;

	public class ItemInfo {
		private Item item;
		private boolean purchased = false;
		private Category category = null;

		private ItemInfo(Item item, boolean isPurchased) {
			this.item = item;
			purchased = isPurchased;
			if (categories != null) {
				for (Category c : categories) {
					if (category != null) {
						break;
					}
					if (c.getItems() != null) {
						for (Item i : c.getItems()) {
							if (i.getId() == item.getId()) {
								category = c;
								break;
							}
						}
					}
				}
			}
		}

		private ItemInfo(Item item, boolean isPurchased, Category category) {
			this.item = item;
			purchased = isPurchased;
			this.category = category;
		}

		public Item getItem() {
			return item;
		}

		public boolean isPurchased() {
			return purchased;
		}

		public Category getCategory() {
			return category;
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

	public static Map<Long, ItemInfo> getItemsByIds() {
		checkInstance();
		return instance.itemsByIds;
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
			Item i) {
		checkInstance();
		return instance._addPurchasedItem(i);
	}

	private boolean _addPurchasedItem(
			Item item) {
		if (item == null) {
			return false;
		}
		ItemInfo itemInfo;
		if (itemsByIds.containsKey(item.getId())) {
			itemInfo = itemsByIds.get(item.getId());
			itemInfo.purchased = true;
		} else {
			itemInfo = new ItemInfo(item, true);
		}
		if (purchasedItems == null) {
			purchasedItems = new ArrayList<ItemManager.ItemInfo>();
			purchasedItems.add(itemInfo);
		} else {
			if (!purchasedItems.contains(itemInfo)) {
				purchasedItems.add(itemInfo);
			}
		}

		if ((availableItems != null)) {
			availableItems.remove(itemInfo);
		}

		if (item.getId() == GARBAGE_COLLECTOR_ID) {
			PowerupManager.setGarbageCollector(true);
		} else if (item.getId() == KILLING_SPREE_ID) {
			PowerupManager.setKillingSpreeEnabled(true);
		}
		return true;
	}

	static void loadPurchasedItems(
			List<Long> purchasedItemsIds) {
		checkInstance();
		instance._loadPurchasedItems(purchasedItemsIds);
	}

	private void _loadPurchasedItems(
			List<Long> purchasedItemsIds) {
		if (purchasedItemsLoaded) {
			return;
		}
		purchasedItemsLoaded = true;
		if (categories == null) {
			return;
		}
		itemsByIds = new HashMap<Long, ItemManager.ItemInfo>();
		if (purchasedItemsIds == null) {
			for (Category c : categories) {
				List<Item> itemsInCategory = c.getItems();
				if (itemsInCategory != null) {
					for (Item i : itemsInCategory) {
						itemsByIds.put(i.getId(), new ItemInfo(i, false));
					}
				}
			}
		} else {
			for (Category c : categories) {
				List<Item> itemsInCategory = c.getItems();
				if (itemsInCategory != null) {
					for (Item i : itemsInCategory) {
						itemsByIds.put(i.getId(), new ItemInfo(i, purchasedItems.contains(i.getId())));
					}
				}
			}
		}
	}

	public static List<ItemInfo> getPurchasedItems() {
		checkInstance();
		return instance._getPurchasedItems();
	}

	private List<ItemInfo> _getPurchasedItems() {
		if (!purchasedItemsLoaded) {
			SharedPreferenceManager.loadPurchasedItems();
		}
		return purchasedItems;
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
		for (ItemInfo itemInfo : availableItems) {
			if (itemInfo.purchased) {
				purchasedItemsIds.add(itemInfo.item.getId());
			}
		}
		return purchasedItemsIds;
	}

	public static List<ItemInfo> getStoreItems() {
		checkInstance();
		return instance.availableItems;
	}

	static Availability isAvailable(
			Item i) {
		checkInstance();
		return instance._isAvailable(i);
	}

	private Availability _isAvailable(
			Item item) {
		if (item == null) {
			return new Availability("Unavailable");
		}
		for (Attribute a : item.getAttributes()) {
			String attributeName = a.getDisplayName();
			if (PowerupManager.UpgradeAttributes.REQUIREMENT.getAttributeName().equals(attributeName)) {
				long requiredItemId = Long.parseLong(a.getValue());
				if ((itemsByIds != null) && (itemsByIds.containsKey(requiredItemId))) {
					ItemInfo info = itemsByIds.get(requiredItemId);
					if (!info.purchased) {
						return new Availability("Requires " + info.item.getDisplayName());
					}
				}
			}
		}
		List<Currency> currencies = GamesPlatformManager.getCurrencies();
		if (currencies == null) {
			return new Availability("Unavailable");
		}
		Map<Currency, Double> prices = item.getItemPrice(currencies);
		for (Entry<Currency, Double> e : prices.entrySet()) {
			Currency c = e.getKey();
			if (c.getDisplayName().equals(FundsManager.PEARLS)) {
				if (FundsManager.getPearls() < e.getValue()) {
					return new Availability(false, true, "Insufficient " + FundsManager.PEARLS);
				}
			} else if (c.getDisplayName().equals(FundsManager.CRYSTALS)) {
				if (FundsManager.getCrystals() < e.getValue()) {
					return new Availability(false, true, "Insufficient " + FundsManager.CRYSTALS);
				}
			}
		}
		return new Availability(true, true);
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

		private Availability(String msg) {
			errorMessage = msg;
		}

		private Availability(boolean isAffordable, boolean isAvailable, String msg) {
			this.isAffordable = isAffordable;
			this.isAvailable = isAvailable;
			errorMessage = msg;
		}

		private Availability(boolean isAffordable, boolean isAvailable) {
			this.isAffordable = isAffordable;
			this.isAvailable = isAvailable;
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

	}
}
