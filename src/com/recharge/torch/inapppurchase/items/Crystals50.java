package com.recharge.torch.inapppurchase.items;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.MarketCategory;

public class Crystals50 extends FundsItem {

	public Crystals50() {
		name = R.string.iap_crystals_50_name;
		description = R.string.iap_crystals_50_description;
		icon = R.drawable.ui_funds_crystal;
		enabled = true;

		reward.put(Funds.CRYSTALS, 50L);
		category = MarketCategory.CRYSTALS;
	}

}
