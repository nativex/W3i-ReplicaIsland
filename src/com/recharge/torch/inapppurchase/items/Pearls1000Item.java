package com.recharge.torch.inapppurchase.items;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;

public class Pearls1000Item extends FundsItem {
	public Pearls1000Item() {
		name = R.string.iap_pearls_1000_name;
		description = R.string.iap_pearls_1000_description;
		icon = R.drawable.ui_funds_pearls;
		enabled = true;

		reward.put(Funds.PEARLS, 1000L);
	}

}
