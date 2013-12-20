package com.recharge.torch.inapppurchase.items;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.MarketCategory;

public class PackSmall extends FundsItem {

	public PackSmall() {
		name = R.string.iap_pack_small_name;
		description = R.string.iap_pack_small_description;
		icon = R.drawable.ui_iap_packs;
		category = MarketCategory.PACKS;

		reward.put(Funds.PEARLS, 750L);
		reward.put(Funds.CRYSTALS, 30L);
	}

}
