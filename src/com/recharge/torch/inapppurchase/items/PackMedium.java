package com.recharge.torch.inapppurchase.items;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.MarketCategory;

public class PackMedium extends FundsItem {

	public PackMedium() {
		name = R.string.iap_pack_medium_name;
		description = R.string.iap_pack_medium_description;
		icon = R.drawable.ui_iap_packs;
		category = MarketCategory.PACKS;

		reward.put(Funds.PEARLS, 1500L);
		reward.put(Funds.CRYSTALS, 75L);
	}
}
