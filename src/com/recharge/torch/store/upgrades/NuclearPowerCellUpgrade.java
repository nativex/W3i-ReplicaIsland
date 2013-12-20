package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class NuclearPowerCellUpgrade extends Upgrade {
	public NuclearPowerCellUpgrade() {
		name = R.string.powerup_shield_nuclear_power_cell_name;
		description = R.string.powerup_shield_nuclear_power_cell_description;
		icon = R.drawable.item_nuclear_power_cell;
		category = StoreCategory.SHIELD;

		attributes.add(Attributes.SHIELD_RECHARGE_REDUCTION.createAttribute(5));

		price.add(Funds.PEARLS.createPrice(1500));
		price.add(Funds.CRYSTALS.createPrice(150));
	}
}
