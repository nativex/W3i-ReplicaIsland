package com.recharge.torch.funds;

import com.recharge.torch.R;
import com.recharge.torch.general.Currency;

public class Pearl extends Currency {

	public Pearl() {
		name = R.string.currency_pearl_name;
		description = R.string.currency_pearl_description;
		amount = 0;
		enabled = true;
		icon = R.drawable.ui_funds_pearls;
	}

	@Override
	public Funds getId() {
		return Funds.PEARLS;
	}

}
