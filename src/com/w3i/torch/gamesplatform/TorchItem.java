package com.w3i.torch.gamesplatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.torch.powerups.PowerupTypes;

public class TorchItem {
	private Item gamesPlatformItem;
	private Category gamesPlatformItemCategory;
	private boolean purchased = false;
	private List<Long> requirements;
	private boolean requirementsRead = false;
	private boolean tracked = false;

	public enum PurchaseState {
		PURCHASED,
		AVAILABLE,
		ANY;
	}

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
		if (gamesPlatformItem != null) {
			return gamesPlatformItem.getItemPriceForCurrency(currency);
		}
		return null;
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

	public void readRequirements() {
		for (Attribute attribute : gamesPlatformItem.getAttributes()) {
			if (PowerupTypes.POWERUP_REQUIREMENT.getDisplayName().equals(attribute.getName())) {
				try {
					long requirementId = Long.parseLong(attribute.getValue());
					if (requirements == null) {
						requirements = new ArrayList<Long>();
					}
					requirements.add(requirementId);
				} catch (Exception e) {
					Log.e("TorchItem: Could not parse requirement. " + getDisplayName() + "->" + attribute.getValue(), e);
				}
			}
		}
		requirementsRead = true;
	}

	public List<Long> getRequirements() {
		if (!requirementsRead) {
			readRequirements();
		}
		return requirements;
	}

	public void updatePowerups() {
		if (purchased) {
			for (Attribute attribute : gamesPlatformItem.getAttributes()) {
				String attributeName = attribute.getName();
				if (PowerupTypes.POWERUP_REQUIREMENT.getDisplayName().equals(attributeName)) {
					continue;
				}
				for (PowerupTypes powerup : PowerupTypes.values()) {
					if (attributeName.equals(powerup.getDisplayName())) {
						try {
							float attributeValue = Float.parseFloat(attribute.getValue());
							if (attributeValue > 0) {
								powerup.addValue(attributeValue);
								powerup.setEnabled(true);
							} else {
								powerup.setEnabled(false);
							}
						} catch (Exception e) {
							Log.e("TorchItem: Failed to parse attribute value. " + getDisplayName() + "->" + attributeName + "->" + attribute.getValue(), e);
						}
					}
				}
			}
		}
	}

	public boolean isTracked() {
		return tracked;
	}

	public void setTracked(
			boolean isTracked) {
		this.tracked = isTracked;
	}

	public boolean hasAttribute(
			PowerupTypes powerup) {
		if (gamesPlatformItem == null) {
			return false;
		}
		for (Attribute attribute : gamesPlatformItem.getAttributes()) {
			if (powerup.getDisplayName().equals(attribute.getDisplayName())) {
				return true;
			}
		}
		return false;
	}

	public String getIcon() {
		return gamesPlatformItem.getStoreImageUrl();
	}

}
