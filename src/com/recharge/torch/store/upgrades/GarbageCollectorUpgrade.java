package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class GarbageCollectorUpgrade extends Upgrade {
	public GarbageCollectorUpgrade() {
		name = R.string.powerup_misc_garbage_collector_name;
		description = R.string.powerup_misc_garbage_collector_description;
		icon = R.drawable.item_garbage_collector;
		category = StoreCategory.MISC;

		attributes.add(Attributes.BONUS_PEARLS.createAttribute(3));

		price.add(Funds.PEARLS.createPrice(750));
		price.add(Funds.CRYSTALS.createPrice(25));
	}

}
