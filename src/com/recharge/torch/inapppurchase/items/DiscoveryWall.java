package com.recharge.torch.inapppurchase.items;

import com.nativex.monetization.MonetizationManager;
import com.recharge.torch.R;
import com.recharge.torch.general.MarketCategory;
import com.recharge.torch.inapppurchase.MarketItem;
import com.recharge.torch.inapppurchase.MarketConstants.BILLING_RESPONSE_CODE;

public class DiscoveryWall extends MarketItem {

	public DiscoveryWall() {
		name = R.string.iap_free_discovery_wall_name;
		description = R.string.iap_free_discovery_wall_description;
		enabled = true;

		category = MarketCategory.FREE;
	}

	@Override
	public void onClicked() {
		MonetizationManager.showOfferWall();
	}

	@Override
	public void onPurchased() {

	}

	@Override
	public void onCanceled() {

	}

	@Override
	public void onFailed(
			BILLING_RESPONSE_CODE response) {

	}

	@Override
	public void onConsumed() {
	}

}
