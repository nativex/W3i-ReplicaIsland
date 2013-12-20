package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class NuclearBatteriesUpgrade extends Upgrade {
	public NuclearBatteriesUpgrade() {
		name = R.string.powerup_jetpack_nuclear_batteries_name;
		description = R.string.powerup_jetpack_nuclear_batteries_description;
		icon = R.drawable.item_battery_small;
		category = StoreCategory.JETPACK;

		requirements.add(Upgrades.TWIN_BATTERIES);
		requirements.add(Upgrades.NUCLEAR_POWER_CELL);

		attributes.add(Attributes.JETPACK_RECHARGE_AIR.createAttribute(100));
		attributes.add(Attributes.JETPACK_RECHARGE_GROUND.createAttribute(100));

		price.add(Funds.PEARLS.createPrice(1500));
		price.add(Funds.CRYSTALS.createPrice(150));
	}
}
