package com.recharge.torch.inapppurchase.items;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;

public class Pearls3000Item extends FundsItem {

	public Pearls3000Item() {
		name = R.string.iap_pearls_3000_name;
		description = R.string.iap_pearls_3000_description;
		icon = R.drawable.ui_funds_pearls;
		enabled = true;

		reward.put(Funds.PEARLS, 3000L);
	}
}
