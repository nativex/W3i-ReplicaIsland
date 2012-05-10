package com.w3i.replica.replicaisland.store;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class StorePurchasedItems {
	@SerializedName("PurchasedItems")
	private ArrayList<Long> items;

	public ArrayList<Long> getItems() {
		return items;
	}

	public void setItems(
			ArrayList<Long> items) {
		this.items = items;
	}

}
