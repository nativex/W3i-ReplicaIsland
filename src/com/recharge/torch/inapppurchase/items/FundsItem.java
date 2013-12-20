package com.recharge.torch.inapppurchase.items;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.recharge.torch.funds.Funds;
import com.recharge.torch.inapppurchase.MarketConstants.BILLING_RESPONSE_CODE;
import com.recharge.torch.inapppurchase.MarketItem;
import com.recharge.torch.inapppurchase.MarketManager;

public class FundsItem extends MarketItem {

	protected Map<Funds, Long> reward;

	public FundsItem() {
		reward = new HashMap<Funds, Long>();
		sku = "android.test.purchased";
	}

	@Override
	public void onClicked() {
		if (MarketManager.isInitialized()) {
			MarketManager.purchaseItem(this);
		}
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
		for (Entry<Funds, Long> entry : reward.entrySet()) {
			Funds key = entry.getKey();
			Long amount = entry.getValue();
			if ((key != null) && (amount != null)) {
				key.addAmount(amount);
			}
		}
	}

}
