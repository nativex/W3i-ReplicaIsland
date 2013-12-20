package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class NanobotsPlatingUpgrade extends Upgrade {
	public NanobotsPlatingUpgrade() {
		name = R.string.powerup_life_ultimate_name;
		description = R.string.powerup_life_large_description;
		icon = R.drawable.life_item_ultimate;
		category = StoreCategory.LIFE;

		requirements.add(Upgrades.CARBON_PLATING);
		requirements.add(Upgrades.NUCLEAR_POWER_CELL);

		attributes.add(Attributes.LIFE.createAttribute(2));

		price.add(Funds.PEARLS.createPrice(1000));
		price.add(Funds.CRYSTALS.createPrice(200));
	}
}
