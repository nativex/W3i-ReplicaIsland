package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class CarbonPlatingUpgrade extends Upgrade {

	public CarbonPlatingUpgrade() {
		name = R.string.powerup_life_large_name;
		description = R.string.powerup_life_large_description;
		icon = R.drawable.item_life_large;
		category = StoreCategory.LIFE;

		attributes.add(Attributes.LIFE.createAttribute(2));

		requirements.add(Upgrades.TWIN_BATTERIES);
		requirements.add(Upgrades.TITANIUM_PLATING);

		price.add(Funds.PEARLS.createPrice(700));
		price.add(Funds.CRYSTALS.createPrice(100));
	}
}
