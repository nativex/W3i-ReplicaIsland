package com.recharge.torch.store.upgrades;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.general.StoreCategory;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;

public class CrystalExtractorUpgrade extends Upgrade {
	public CrystalExtractorUpgrade() {
		name = R.string.powerup_misc_crystal_extractor_name;
		description = R.string.powerup_misc_crystal_extractor_description;
		icon = R.drawable.item_crystal_extractor;
		attributes.add(Attributes.BONUS_CRYSTALS_CHANCE.createAttribute(25));
		category = StoreCategory.MISC;

		requirements.add(Upgrades.KILLING_SPREE);
		requirements.add(Upgrades.NUCLEAR_POWER_CELL);

		price.add(Funds.PEARLS.createPrice(1200));
		price.add(Funds.CRYSTALS.createPrice(40));
	}
}
