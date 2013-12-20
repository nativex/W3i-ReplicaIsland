package com.recharge.torch.inapppurchase;

import android.content.Intent;

import com.google.gson.Gson;
import com.recharge.torch.inapppurchase.MarketConstants.PURCHASE_STATE;

public class InAppPurchaseData {
	private String orderId;
	private String packageName;
	private String productId;
	private Long purchaseTime;
	private Integer purchaseState;
	private String developerPayload;
	private String purchaseToken;
	private String signedData;
	private String signature;

	static final InAppPurchaseData parseJson(
			String json,
			String signature) {
		try {
			InAppPurchaseData purchase = new Gson().fromJson(json, InAppPurchaseData.class);
			purchase.signedData = json;
			purchase.signature = signature;
			return purchase;
		} catch (Exception e) {
			return null;
		}
	}

	static final InAppPurchaseData parseJson(
			Intent data) {
		try {
			String json = data.getStringExtra(MarketConstants.PURCHASE_DATA);
			String dataSignature = data.getStringExtra(MarketConstants.PURCHASE_SIGNATURE);
			return parseJson(json, dataSignature);
		} catch (Exception e) {
			return null;
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getProductId() {
		return productId;
	}

	public Long getPurchaseTime() {
		return purchaseTime;
	}

	public PURCHASE_STATE getPurchaseState() {
		return PURCHASE_STATE.getState(purchaseState);
	}

	public String getDeveloperPayload() {
		return developerPayload;
	}

	public String getPurchaseToken() {
		return purchaseToken;
	}

	public String getSignedData() {
		return signedData;
	}

	public String getSignature() {
		return signature;
	}

}
