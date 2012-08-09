package com.w3i.torch.gamesplatform;

import java.util.List;
import java.util.Map;

import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class TorchItem {
	private Item gamesPlatformItem;
	private Category gamesPlatformItemCategory;
	private boolean purchased = false;
	private List<Long> requirements;

	public TorchItem(Category itemCatrgory, Item item) {
		gamesPlatformItem = item;
		gamesPlatformItemCategory = itemCatrgory;
	}

	public String getDisplayName() {
		return gamesPlatformItem.getDisplayName();
	}

	public String getCategoryName() {
		return gamesPlatformItemCategory.getDisplayName();
	}

	public String getDescription() {
		return gamesPlatformItem.getDescription();
	}

	public long getId() {
		return gamesPlatformItem.getId();
	}

	public Map<Currency, Double> getItemPrice(
			List<Currency> currencies) {
		return gamesPlatformItem.getItemPrice(currencies);
	}

	public Double getItemPrice(
			Currency currency) {
		return gamesPlatformItem.getItemPriceForCurrency(currency);
	}

	public Item getItem() {
		return gamesPlatformItem;
	}

	public Category getCategory() {
		return gamesPlatformItemCategory;
	}

	public long getCategoryId() {
		return gamesPlatformItemCategory.getId();
	}

	public boolean isPurchased() {
		return purchased;
	}

	public void setPurchased(
			boolean purchased) {
		this.purchased = purchased;
	}

	public List<Long> getRequirements() {
		return requirements;
	}

	public String getIcon() {
		return gamesPlatformItem.getStoreImageUrl();
	}

}
