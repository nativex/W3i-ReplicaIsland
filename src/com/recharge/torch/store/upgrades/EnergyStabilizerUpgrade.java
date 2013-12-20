package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class EnergyStabilizerUpgrade extends Upgrade {

	public EnergyStabilizerUpgrade() {
		name = R.string.powerup_shield_energy_stabilizer_name;
		description = R.string.powerup_shield_energy_stabilizer_description;
		icon = R.drawable.item_shield;
		attributes.add(Attributes.SHIELD_DURATION.createAttribute(5));
		category = StoreCategory.SHIELD;
		price.add(Funds.PEARLS.createPrice(2000));
		price.add(Funds.CRYSTALS.createPrice(150));
	}
}
