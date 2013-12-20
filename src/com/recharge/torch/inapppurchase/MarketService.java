package com.recharge.torch.inapppurchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.android.vending.billing.IInAppBillingService;
import com.recharge.torch.inapppurchase.MarketConstants.BILLING_RESPONSE_CODE;
import com.recharge.torch.inapppurchase.MarketConstants.PURCHASE_TYPE;

public class MarketService {
	private ServiceConnection marketConnection = new MarketServiceConnection();
	private IInAppBillingService marketService;
	private boolean subsSupported = false;
	private boolean inAppPurchaseSupported = false;

	void bind(
			Activity activity) {

		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		if (!activity.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty()) {
			// service available to handle that Intent
			if (activity.bindService(serviceIntent, marketConnection, Context.BIND_AUTO_CREATE)) {
				return;
			}
		}
		MarketManager.setInitialized(false);
	}

	public void purchaseItem(
			Activity activity,
			MarketItem item) {
		try {
			Bundle buyIntentBundle = marketService.getBuyIntent(3, activity.getPackageName(), item.getSku(), item.getPurchaseType().getId(), item.getDeveloperPayload());
			BILLING_RESPONSE_CODE responseCode = BILLING_RESPONSE_CODE.getResponseCode(buyIntentBundle);
			if (responseCode == BILLING_RESPONSE_CODE.RESULT_OK) {
				PendingIntent pendingIntent = buyIntentBundle.getParcelable(MarketConstants.BUY_INTENT);
				activity.startIntentSenderForResult(pendingIntent.getIntentSender(), item.getRequestCode(), new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
				MarketManager.registerItemForPurchase(item);
				return;
			}
		} catch (Exception e) {
			MarketManager.logError("Failed to purchase item", e);
		}
		item.onFailed(BILLING_RESPONSE_CODE.RESULT_ERROR);
	}

	public void handleActivityResult(
			MarketItem item,
			int resultCode,
			Intent data) {
		switch (resultCode) {
			case Activity.RESULT_OK:
				handleResponse(item, data);
				break;
			case Activity.RESULT_CANCELED:
				item.onCanceled();
				break;
			default:
				item.onFailed(BILLING_RESPONSE_CODE.RESULT_ERROR);
				break;
		}
	}

	private void handleResponse(
			MarketItem item,
			Intent data) {
		if (validateResponseCode(item, data)) {
			item.setPurchase(InAppPurchaseData.parseJson(data));
			if (validatePurchase(item)) {
				handlePurchase(item);
			}
		}
	}

	private boolean validateResponseCode(
			MarketItem item,
			Intent data) {
		BILLING_RESPONSE_CODE responseCode = BILLING_RESPONSE_CODE.getResponseCode(data);
		switch (responseCode) {
			case RESULT_OK:
				return true;

			case RESULT_USER_CANCELED:
				item.onCanceled();
				return false;

			default:
				item.onFailed(responseCode);
				return false;
		}
	}

	private boolean validatePurchase(
			MarketItem item) {
		InAppPurchaseData data = item.getPurchase();
		if (data == null) {
			item.onFailed(BILLING_RESPONSE_CODE.RESULT_ERROR);
			return false;
		}
		boolean isValid = true;
		if (item.getDeveloperPayload() != null) {
			if (!item.getDeveloperPayload().equals(data.getDeveloperPayload())) {
				isValid = false;
			}

		} else if (data.getDeveloperPayload() != null) {
			isValid = false;
		}
		if ((isValid) && (Security.verifyPurchase(MarketManager.getPublicKey(), data.getSignedData(), data.getSignature()))) {
			return true;
		} else {
			item.onFailed(BILLING_RESPONSE_CODE.RESULT_ITEM_VERIFICATION_FAILED);
			return false;
		}
	}

	private void checkBillingSupport() {
		try {
			inAppPurchaseSupported = checkBillingSupported(PURCHASE_TYPE.IN_APP) == BILLING_RESPONSE_CODE.RESULT_OK;
			subsSupported = ((inAppPurchaseSupported) && (checkBillingSupported(PURCHASE_TYPE.SUBSCRIPTION) == BILLING_RESPONSE_CODE.RESULT_OK));
			MarketManager.setInitialized(true);
		} catch (Exception e) {
			MarketManager.setInitialized(false);
			MarketManager.logError("Failed the check for billing support", e);
		}
	}

	private BILLING_RESPONSE_CODE checkBillingSupported(
			MarketConstants.PURCHASE_TYPE itemType) throws Exception {
		int responseCode = marketService.isBillingSupported(3, MarketManager.getContext().getPackageName(), itemType.getId());
		return BILLING_RESPONSE_CODE.getResponseCode(responseCode);
	}

	void dispose(
			Activity activity) {
		try {
			if (marketService != null) {
				activity.unbindService(marketConnection);
			}
		} catch (Exception e) {
			MarketManager.logError("Failed to unbind service", e);
		}
	}

	public boolean isBillingSupported(
			PURCHASE_TYPE type) {
		if (marketService == null) {
			return false;
		}
		switch (type) {
			case IN_APP:
				return inAppPurchaseSupported;
			case SUBSCRIPTION:
				return subsSupported;
		}
		return false;
	}

	private void reset() {
		marketService = null;
		inAppPurchaseSupported = false;
		subsSupported = false;
	}

	private void handlePurchase(
			MarketItem item) {
		item.onPurchased();
	}

	public boolean isConnected() {
		return marketService != null;
	}

	public void consumePurchase(
			final MarketItem item) {
		if ((item == null) || (item.getPurchase() == null)) {
			return;
		}
		if (isBillingSupported(item.getPurchaseType())) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					consumeAsync(item);
				}
			}).start();
		} else {
			item.onFailed(BILLING_RESPONSE_CODE.RESULT_BILLING_UNAVAILABLE);
		}
	}

	public void consumeAsyncMultiple(
			final List<MarketItem> items) {
		if (items == null) {
			return;
		}
		for (int i = 0; i < items.size(); i++) {
			final MarketItem item = items.get(i);
			try {
				int response = marketService.consumePurchase(3, MarketManager.getContext().getPackageName(), item.getPurchase().getPurchaseToken());
				final BILLING_RESPONSE_CODE responseCode = BILLING_RESPONSE_CODE.getResponseCode(response);
				MarketManager.handler.post(new Runnable() {
					public void run() {
						if (responseCode == BILLING_RESPONSE_CODE.RESULT_OK) {
							item.onConsumed();
						} else {
							item.onFailed(responseCode);
						}
					}
				});

			} catch (Exception e) {
				MarketManager.handler.post(new Runnable() {
					@Override
					public void run() {
						item.onFailed(BILLING_RESPONSE_CODE.RESULT_ERROR);
					}
				});
				MarketManager.logError("Failed to consume a purchase", e);
			}
		}
	}

	public void consumeAsync(
			final MarketItem item) {
		try {
			int response = marketService.consumePurchase(3, MarketManager.getContext().getPackageName(), item.getPurchase().getPurchaseToken());
			final BILLING_RESPONSE_CODE responseCode = BILLING_RESPONSE_CODE.getResponseCode(response);
			MarketManager.handler.post(new Runnable() {
				public void run() {
					if (responseCode == BILLING_RESPONSE_CODE.RESULT_OK) {
						item.onConsumed();
					} else {
						item.onFailed(responseCode);
					}
				}
			});
		} catch (Exception e) {
			MarketManager.handler.post(new Runnable() {
				@Override
				public void run() {
					item.onFailed(BILLING_RESPONSE_CODE.RESULT_ERROR);
				}
			});
			MarketManager.logError("Failed to consume a purchase", e);
		}
	}

	private void queryPurchases() {
		try {
			String continuationToken = null;
			Map<String, InAppPurchaseData> purchases = new HashMap<String, InAppPurchaseData>();
			do {
				Bundle response = marketService.getPurchases(3, MarketManager.getContext().getPackageName(), PURCHASE_TYPE.IN_APP.getId(), null);
				BILLING_RESPONSE_CODE responseCode = BILLING_RESPONSE_CODE.getResponseCode(response);

				if (responseCode == BILLING_RESPONSE_CODE.RESULT_OK) {
					List<String> ownedSkus = response.getStringArrayList(MarketConstants.PURCHASE_ITEM_LIST);
					List<String> purchaseDataList = response.getStringArrayList(MarketConstants.PURCHASE_DATA_LIST);
					List<String> signatureList = response.getStringArrayList(MarketConstants.DATA_SIGNATURE_LIST);
					for (int i = 0; i < purchaseDataList.size(); ++i) {
						String purchaseData = purchaseDataList.get(i);
						String signature = signatureList.get(i);
						String sku = ownedSkus.get(i);
						if (Security.verifyPurchase(MarketManager.getPublicKey(), purchaseData, signature)) {
							InAppPurchaseData purchase = InAppPurchaseData.parseJson(purchaseData, signature);
							purchases.put(sku, purchase);
						}
					}
					continuationToken = response.getString(MarketConstants.CONTINUATION_TOKEN);
				} else {
					MarketManager.logError("Failed to reterieve purchases. " + responseCode.getMessage(), null);
					MarketManager.fireOnQueryPurchasesCompleted(false);
					return;
				}
			} while (continuationToken != null);
			MarketManager.setPurchasedItems(purchases);
			MarketManager.fireOnQueryPurchasesCompleted(true);
		} catch (Exception e) {
			MarketManager.logError("Failed to reterieve purchases. ", e);
			MarketManager.fireOnQueryPurchasesCompleted(false);
		}
	}

	private class MarketServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(
				ComponentName name,
				IBinder service) {
			marketService = IInAppBillingService.Stub.asInterface(service);
			checkBillingSupport();
			queryPurchases();
		}

		@Override
		public void onServiceDisconnected(
				ComponentName name) {
			reset();
		}

	}

}
