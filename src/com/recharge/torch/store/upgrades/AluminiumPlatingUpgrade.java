package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class AluminiumPlatingUpgrade extends Upgrade {
	public AluminiumPlatingUpgrade() {
		name = R.string.powerup_life_small_name;
		description = R.string.powerup_life_small_description;
		icon = R.drawable.item_life_small;
		attributes.add(Attributes.LIFE.createAttribute(1));
		price.add(Funds.PEARLS.createPrice(200));
		category = StoreCategory.LIFE;
	}
}
