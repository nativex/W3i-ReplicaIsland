package com.recharge.torch.funds;

import com.recharge.torch.R;
import com.recharge.torch.general.Currency;

public class Crystal extends Currency {

	public Crystal() {
		name = R.string.currency_crystal_name;
		description = R.string.currency_crystal_description;
		icon = R.drawable.ui_funds_crystal;
		amount = 0;
		enabled = true;
	}

	@Override
	public Funds getId() {
		return Funds.CRYSTALS;
	}

}