package com.recharge.torch.general;

import com.recharge.torch.funds.Funds;

public interface OnCurrencyChanged {
	public void currencyChanged(
			Funds currency);
}