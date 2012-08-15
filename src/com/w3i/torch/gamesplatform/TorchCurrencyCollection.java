package com.w3i.torch.gamesplatform;

import java.util.HashMap;

public class TorchCurrencyCollection extends HashMap<Long, TorchCurrency> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5568901445368763491L;

	public TorchCurrency findCurrency(
			String name) {
		if (name == null) {
			return null;
		}
		for (Entry<Long, TorchCurrency> entry : entrySet()) {
			TorchCurrency currency = entry.getValue();
			if (name.equals(currency.getAlternateId())) {
				return currency;
			}
		}
		return null;
	}

}