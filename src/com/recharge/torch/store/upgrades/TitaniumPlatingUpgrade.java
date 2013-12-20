package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class TitaniumPlatingUpgrade extends Upgrade {
	public TitaniumPlatingUpgrade() {
		name = R.string.powerup_life_medium_name;
		description = R.string.powerup_life_medium_description;
		icon = R.drawable.item_life_medium;
		category = StoreCategory.LIFE;

		requirements.add(Upgrades.ALUMINIUM_PLATING);

		attributes.add(Attributes.LIFE.createAttribute(1));

		price.add(Funds.PEARLS.createPrice(500));
	}
}
