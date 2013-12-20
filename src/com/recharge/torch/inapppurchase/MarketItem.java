package com.recharge.torch.inapppurchase;

import com.recharge.torch.general.BaseItem;
import com.recharge.torch.general.MarketCategory;
import com.recharge.torch.inapppurchase.MarketConstants.BILLING_RESPONSE_CODE;
import com.recharge.torch.inapppurchase.MarketConstants.PURCHASE_TYPE;

public abstract class MarketItem extends BaseItem {
	protected MarketCategory category;
	protected MarketItems itemType = MarketItems.UNAVAILABLE;
	protected String sku = "android.test.item_unavailable";
	protected MarketConstants.PURCHASE_TYPE purchaseType = PURCHASE_TYPE.IN_APP;
	protected String developerPayload;
	private int requestCode;
	private static int incrementalCode = 1234;
	private InAppPurchaseData purchaseData;
	protected boolean consumable = true;

	public MarketItem() {
		requestCode = incrementalCode;
		incrementalCode++;
	}

	public abstract void onClicked();

	public void onPurchased() {
		if (consumable) {
			MarketManager.consumeItem(this);
		}
	}

	public abstract void onCanceled();

	public abstract void onFailed(
			BILLING_RESPONSE_CODE response);

	public void onConsumed() {
		MarketManager.save(this);
	}

	public MarketCategory getCategory() {
		if (category == null) {
			return MarketCategory.MISC;
		}
		return category;
	}

	public String getSku() {
		return itemType.getMarketId();
	}

	public MarketConstants.PURCHASE_TYPE getPurchaseType() {
		return purchaseType;
	}
	
	public MarketItems getItemType() {
		return itemType;
	}

	public String getDeveloperPayload() {
		return developerPayload;
	}

	public int getRequestCode() {
		return requestCode;
	}

	void setPurchase(
			InAppPurchaseData purchase) {
		this.purchaseData = purchase;
	}

	public InAppPurchaseData getPurchase() {
		return purchaseData;
	}
}
