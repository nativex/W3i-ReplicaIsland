package com.recharge.torch.inapppurchase;

import android.content.Intent;
import android.os.Bundle;

public class MarketConstants {
	public static final String LOG_TAG = "Torch Market";
	public static final String BILLING_RESPONSE = "RESPONSE_CODE";
	public static final String BUY_INTENT = "BUY_INTENT";
	public static final String PURCHASE_DATA = "INAPP_PURCHASE_DATA";
	public static final String PURCHASE_SIGNATURE = "INAPP_DATA_SIGNATURE";
	public static final String PURCHASE_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
	public static final String PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
	public static final String DATA_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
	public static final String CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
	public static final String SKU_ITEM_LIST = "ITEM_ID_LIST";

	public enum BILLING_RESPONSE_CODE {
		RESULT_OK(0, "Success"),
		RESULT_USER_CANCELED(1, "User pressed back or canceled a dialog"),
		RESULT_BILLING_UNAVAILABLE(3, "Billing API version is not supported for the type requested"),
		RESULT_ITEM_UNAVAILABLE(4, "Requested product is not available for purchase"),
		RESULT_DEVELOPER_ERROR(5, "Invalid arguments provided to the API. This error can also indicate that the application was not correctly signed or properly set up for In-app Billing in Google Play, or does not have the necessary permissions in its manifest"),
		RESULT_ERROR(6, "Fatal error during the API action"),
		RESULT_ITEM_ALREADY_OWNED(7, "Failure to purchase since item is already owned"),
		RESULT_ITEM_NOT_OWNED(8, "Failure to consume since item is not owned"),
		RESULT_ITEM_VERIFICATION_FAILED(9, "The item purchased item verification failed");

		private int code;
		private String message;

		private BILLING_RESPONSE_CODE(int responseCode, String responseMessage) {
			code = responseCode;
			message = responseMessage;
		}

		public String getMessage() {
			return message;
		}

		public int getCode() {
			return code;
		}

		public static BILLING_RESPONSE_CODE getResponseCode(
				int code) {
			for (BILLING_RESPONSE_CODE responseCode : values()) {
				if (code == responseCode.getCode()) {
					return responseCode;
				}
			}
			return RESULT_ERROR;
		}

		/**
		 * Workaround to bug where sometimes response codes come as Long instead of Integer
		 * 
		 * @param bundle
		 *            {@link Bundle}. The service response.
		 * @return
		 */
		public static BILLING_RESPONSE_CODE getResponseCode(
				Bundle bundle) {
			if (bundle != null) {
				Object o = bundle.get(MarketConstants.BILLING_RESPONSE);
				if (o == null) {
					return RESULT_OK;
				} else if (o instanceof Integer) {
					return getResponseCode(((Integer) o).intValue());
				} else if (o instanceof Long) {
					return getResponseCode((int) ((Long) o).longValue());
				}
			}
			return RESULT_ERROR;
		}

		/**
		 * Workaround to bug where sometimes response codes come as Long instead of Integer
		 * 
		 * @param intent
		 *            {@link Intent}. The service response.
		 * @return
		 */
		public static BILLING_RESPONSE_CODE getResponseCode(
				Intent intent) {
			if (intent != null) {
				return getResponseCode(intent.getExtras());
			}
			return RESULT_ERROR;
		}
	}

	public enum PURCHASE_STATE {
		UNKNOWN(-1),
		PURCHASED(0),
		CANCELLED(1),
		REFUNDED(2);

		private int state;

		private PURCHASE_STATE(int state) {
			this.state = state;
		}

		public int getState() {
			return state;
		}

		public static PURCHASE_STATE getState(
				int state) {
			for (PURCHASE_STATE purchaseState : values()) {
				if (purchaseState.getState() == state) {
					return purchaseState;
				}
			}
			return UNKNOWN;
		}
	}

	public enum PURCHASE_TYPE {
		IN_APP("inapp"),
		SUBSCRIPTION("subs");

		private String type;

		private PURCHASE_TYPE(String type) {
			this.type = type;
		}

		public String getId() {
			return type;
		}
	}
}
