package com.w3i.replica.replicaisland.store;

public class PurchaseInformation {
	private int livesUpgrade = 0;
	private static PurchaseInformation instance;
	
	private PurchaseInformation() {
	}
	
	private static void init() {
		if (instance == null) {
			instance = new PurchaseInformation();
		}
	}
	
	public static int getLivesUpgrade() {
		init();
		return instance.livesUpgrade;
	}
	
}
