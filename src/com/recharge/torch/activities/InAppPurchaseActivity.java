package com.recharge.torch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.recharge.torch.R;

public class InAppPurchaseActivity extends Activity {
	// private InAppPurchaseList container;

	private View.OnClickListener onItemClick = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			// if (v instanceof InAppPurchaseItem) {
			// Item item = ((InAppPurchaseItem) v).getGamesPlatformItem();
			// if (item.getType().equals(ItemType.IN_APP_PURCHASE.getValue())) {
			// TorchInAppPurchaseManager.buyItem(InAppPurchaseActivity.this, item);
			// } else {
			// if (item.getDisplayName().equals("Discovery Wall")) {
			// MonetizationManager.showOfferWall();
			// }
			// }
			// }
		}
	};

	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_in_app_purchase);
		// container = (InAppPurchaseList) findViewById(R.id.ui_activity_iap_list);
		// container.setOnItemClickListener(onItemClick);
		// container.loadCategories(TorchInAppPurchaseManager.getCategories());
	}
}
