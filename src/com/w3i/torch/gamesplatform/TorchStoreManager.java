package com.w3i.torch.gamesplatform;

public class TorchStoreManager {

	private TorchStoreManager() {

	}

	public static void purchaseItem(
			TorchItem item) {
		TorchItemManager.purchaseItem(item);
	}

	public static void purchaseItem(
			long itemId) {
		TorchItemManager.purchaseItem(itemId);
		
	}
	
	
}
