package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.replica.replicaisland.achivements.Achievement.Type;
import com.w3i.replica.replicaisland.achivements.AchievementManager;

public class ItemManager {
	private static final long GARBAGE_COLLECTOR_ID = 1049;
	private static final long KILLING_SPREE_ID = 1050;
	private static final long ULTIMATE_HEALTH_ID = 1004;
	private static final long CRYSTAL_EXTRACTOR = 1051;
	private static ItemManager instance;
	private List<Category> categories;
	private Map<Long, Item> itemsByIds;
	private List<Item> purchasedItems;
	private List<Item> availableItems;

	public class ItemInfo {
		private Item item;
		private boolean purchased = false;
		private Category category = null;
		private List<Long> requirements = null;

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

		private List<Long> getRequirement() {
			if (requirements != null) {
				Log.i("ItemManager: " + item.getDisplayName() + "'s requirements already read");
				return requirements;
			}
			try {
				List<Attribute> attributes = item.getAttributes();
				requirements = new ArrayList<Long>();

				if (attributes != null) {
					for (Attribute a : attributes) {
						if (PowerupManager.UpgradeAttributes.REQUIREMENT.getAttributeName().equals(a.getName())) {
							requirements.add(Long.parseLong(a.getValue()));
						}
					}
				}
			} catch (Exception e) {
				Log.e("ItemManager: Could not parse item " + item.getDisplayName() + " requirement.", e);
			}
			return requirements;
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

	public static Map<Long, Item> getItemsByIds() {
		checkInstance();
		return instance.itemsByIds;
	}

	static void setCategories(
			List<Category> categories) {
		checkInstance();
		instance.categories = categories;
		instance.loadItems();
	}

	private void loadItems() {
		if (categories == null) {
			return;
		}

		itemsByIds = new HashMap<Long, Item>();
		availableItems = new ArrayList<Item>();

		for (Category c : categories) {
			List<Item> items = c.getItems();
			sortArray(items);
			if (items != null) {
				for (Item i : items) {
					ItemInfo itemInfo = new ItemInfo(i, false, c);
					i.setTag(itemInfo);
					itemsByIds.put(i.getId(), i);
					availableItems.add(i);
				}
			}

		}
	}

	private void sortArray(
			List<Item> array) {
		Collections.sort(array, new Comparator<Item>() {
			public int compare(
					Item object1,
					Item object2) {
				if (object1.getId() < object2.getId()) {
					return -1;
				} else if (object1.getId() > object2.getId()) {
					return 1;
				}
				return 0;
			}
		});
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
		ItemInfo itemInfo = null;
		try {
			itemInfo = (ItemInfo) item.getTag();
		} catch (Exception e) {
			Log.e("ItemManager: Failed to mark " + item.getDisplayName() + " as purchased.", e);
		}
		if (itemInfo != null) {
			itemInfo.purchased = true;
		}
		if (purchasedItems == null) {
			purchasedItems = new ArrayList<Item>();
			purchasedItems.add(item);
		} else {
			if (!purchasedItems.contains(item)) {
				purchasedItems.add(item);
			}
		}

		if ((availableItems != null)) {
			availableItems.remove(item);
		}

		if (item.getId() == GARBAGE_COLLECTOR_ID) {
			PowerupManager.setGarbageCollector(true);
		} else if (item.getId() == KILLING_SPREE_ID) {
			PowerupManager.setKillingSpreeEnabled(true);
		} else if (item.getId() == CRYSTAL_EXTRACTOR) {
			AchievementManager.setAchievementLocked(Type.BONUS_CRYSTALS, false);
		} else if (item.getId() == ULTIMATE_HEALTH_ID) {
			AchievementManager.setAchievementDone(Type.HEALTH, true);
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
		if (categories == null) {
			return;
		}

		if ((purchasedItemsIds == null) || (purchasedItemsIds.size() <= 0)) {
			return;
		}

		if (purchasedItems == null) {
			purchasedItems = new ArrayList<Item>();
		}

		for (Long id : purchasedItemsIds) {
			Item i = itemsByIds.get(id);
			if (i != null) {
				if (availableItems != null) {
					availableItems.remove(i);
				}
				if (!purchasedItems.contains(i)) {
					if (i.getTag() instanceof ItemInfo) {
						ItemInfo info = (ItemInfo) i.getTag();
						info.purchased = true;
						purchasedItems.add(i);
					}
				}

			}
		}

	}

	public static List<Item> getPurchasedItems() {
		checkInstance();
		return instance._getPurchasedItems();
	}

	private List<Item> _getPurchasedItems() {
		SharedPreferenceManager.loadPurchasedItems();
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
		for (Item item : purchasedItems) {
			purchasedItemsIds.add(item.getId());
		}
		return purchasedItemsIds;
	}

	public static List<Item> getStoreItems() {
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
		ItemInfo info = null;
		if (item.getTag() instanceof ItemInfo) {
			info = (ItemInfo) item.getTag();
		}
		if (info == null) {
			Log.e("ItemManager: Could not get item info of " + item.getDisplayName() + ". Failed to check item availibility");
			return new Availability("Unavailable");
		}
		List<Long> requirements = info.getRequirement();
		if (requirements.size() > 0) {
			for (Long id : requirements) {
				Item i = itemsByIds.get(id);
				if (i != null) {
					if ((purchasedItems == null) || (!purchasedItems.contains(i))) {
						return new Availability(false, false, "Requires " + i.getDisplayName());
					}
				}
			}
		}
		List<Currency> currencies = GamesPlatformManager.getCurrencies();
		if (currencies == null) {
			Log.e("ItemManager: No Games Platform currencies stored. Failed to check if the item is affordable");
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
