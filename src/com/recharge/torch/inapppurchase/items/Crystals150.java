package com.recharge.torch.inapppurchase.items;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.MarketCategory;

public class Crystals150 extends FundsItem {

	public Crystals150() {
		name = R.string.iap_crystals_150_name;
		description = R.string.iap_crystals_150_description;
		icon = R.drawable.ui_funds_crystal;
		enabled = true;

		reward.put(Funds.CRYSTALS, 150L);
		category = MarketCategory.CRYSTALS;
	}
}
