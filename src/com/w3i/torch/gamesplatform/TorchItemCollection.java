package com.w3i.torch.gamesplatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.w3i.torch.gamesplatform.TorchItem.PurchaseState;
import com.w3i.torch.powerups.PowerupTypes;

public class TorchItemCollection extends HashMap<Long, TorchItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2236982711843803793L;

	public void reloadItemPowerups() {
		for (PowerupTypes powerup : PowerupTypes.values()) {
			powerup.setEnabled(false);
			powerup.setValue(0);
		}
		for (Entry<Long, TorchItem> entry : entrySet()) {
			TorchItem item = entry.getValue();
			if (item != null) {
				item.updatePowerups();
			}
		}
	}

	public List<TorchItem> getItems(
			PurchaseState state) {
		List<TorchItem> items = null;
		switch (state) {
		case ANY:
			items = getItems();
			break;
		case AVAILABLE:
			items = getItems(true);
			break;
		case PURCHASED:
			items = getItems(false);
			break;

		}
		return items;
	}

	private List<TorchItem> getItems() {
		List<TorchItem> items = new ArrayList<TorchItem>();
		for (Entry<Long, TorchItem> entry : entrySet()) {
			items.add(entry.getValue());
		}
		return items;
	}

	public List<TorchItem> getItems(
			boolean purchased) {
		List<TorchItem> availableItems = new ArrayList<TorchItem>();
		for (Entry<Long, TorchItem> entry : entrySet()) {
			TorchItem item = entry.getValue();
			if (item.isPurchased() == purchased) {
				availableItems.add(item);
			}
		}
		return availableItems;
	}

	public Map<TorchItem.PurchaseState, List<TorchItem>> getAllItems() {
		List<TorchItem> availableItems = new ArrayList<TorchItem>();
		List<TorchItem> purchasedItems = new ArrayList<TorchItem>();
		for (Entry<Long, TorchItem> entry : entrySet()) {
			TorchItem item = entry.getValue();
			if (item.isPurchased()) {
				purchasedItems.add(item);
			} else {
				availableItems.add(item);
			}
		}
		Map<TorchItem.PurchaseState, List<TorchItem>> allItems = new HashMap<TorchItem.PurchaseState, List<TorchItem>>(2);
		allItems.put(PurchaseState.AVAILABLE, availableItems);
		allItems.put(PurchaseState.PURCHASED, purchasedItems);
		return allItems;
	}
}
