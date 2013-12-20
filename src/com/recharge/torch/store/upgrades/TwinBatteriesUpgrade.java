package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class TwinBatteriesUpgrade extends Upgrade {

	public TwinBatteriesUpgrade() {
		name = R.string.powerup_jetpack_twin_batteries_name;
		description = R.string.powerup_jetpack_twin_batteries_description;
		icon = R.drawable.item_battery_large;
		category = StoreCategory.JETPACK;

		attributes.add(Attributes.JETPACK_DURATION.createAttribute(50));

		price.add(Funds.PEARLS.createPrice(1500));
		price.add(Funds.CRYSTALS.createPrice(100));
	}
}
