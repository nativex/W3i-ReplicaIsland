package com.recharge.torch.general;

import java.util.ArrayList;
import java.util.List;

import com.recharge.torch.store.upgrades.Upgrades;

public abstract class Upgrade extends BaseItem {
	protected List<Currency> price;
	protected List<Upgrades> requirements;
	protected List<Attribute> attributes;
	protected int icon;
	protected StoreCategory category = StoreCategory.MISC;

	public Upgrade() {
		price = new ArrayList<Currency>();
		requirements = new ArrayList<Upgrades>();
		attributes = new ArrayList<Attribute>();
	}

	public List<Currency> getPrice() {
		return price;
	}

	public List<Upgrades> getRequirements() {
		return requirements;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public int getIcon() {
		return icon;
	}

	public StoreCategory getCategory() {
		return category;
	}

	public boolean isAffordable() {
		for (Currency curr : price) {
			if (curr.getAmount() > curr.getId().getAmount()) {
				return false;
			}
		}
		return true;
	}

	public void consume() {
		for (Attribute a : attributes) {
			a.consume();
		}
	}

	public void buy() {
		for (Currency c : price) {
			c.getId().substractAmount(c.getAmount());
		}
	}

	public boolean hasMetRequirements() {
		for (Upgrades upgrade : requirements) {
			if (!upgrade.isOwned()) {
				return false;
			}
		}
		return true;
	}
}
