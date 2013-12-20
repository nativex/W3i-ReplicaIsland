package com.recharge.torch.general;

import com.recharge.torch.R;
import com.recharge.torch.funds.Funds;

public abstract class Currency extends BaseItem {
	protected long amount = 0;

	public Currency() {
		name = R.string.currency_default_name;
		description = R.string.currency_default_description;
	}

	public int getIcon() {
		return icon;
	}

	public void add(
			int amount) {
		this.amount += amount;
	}

	public void set(
			long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public abstract Funds getId();

}
