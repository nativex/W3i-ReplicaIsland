package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class KillingSpreeUpgrade extends Upgrade {
	public KillingSpreeUpgrade() {
		name = R.string.powerup_misc_killing_spree_name;
		description = R.string.powerup_misc_killing_spree_description;
		icon = R.drawable.item_killing_spree;
		category = StoreCategory.MISC;

		requirements.add(Upgrades.GARBAGE_COLLECTOR);

		attributes.add(Attributes.BONUS_PEARLS_MULTIPLIER.createAttribute(150));

		price.add(Funds.PEARLS.createPrice(500));
		price.add(Funds.CRYSTALS.createPrice(50));
	}
}
