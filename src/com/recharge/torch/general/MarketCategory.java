package com.recharge.torch.general;

import com.recharge.torch.R;

public enum MarketCategory {
	PEARLS(R.string.category_market_pearls, R.drawable.ui_iap_pearls),
	CRYSTALS(R.string.category_market_crystals, R.drawable.ui_iap_crystals),
	FREE(R.string.category_market_free, 0),
	PACKS(R.string.category_market_packs, R.drawable.ui_iap_packs),
	MISC(R.string.category_market_misc, 0);

	private int name = R.string.item_unknown_name;
	private int icon = R.drawable.ui_funds_unknown;

	private MarketCategory(int name, int icon) {
		if (name > 0) {
			this.name = name;
		}
		if (icon > 0) {
			this.icon = icon;
		}
	}

	public int getName() {
		return name;
	}

	public int getIcon() {
		return icon;
	}
}
