package com.w3i.replica.replicaisland.store;

public class Item<T> {
	private T powerupValue;
	private String name;
	private String description;
	private int icon;
	private long price;

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public T getPowerupValue() {
		return powerupValue;
	}

	protected void setPowerupValue(T value) {
		this.powerupValue = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

}
