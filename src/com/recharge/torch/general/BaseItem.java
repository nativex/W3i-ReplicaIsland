package com.recharge.torch.general;

import com.recharge.torch.R;

public abstract class BaseItem {
	protected int name = R.string.item_unknown_name;
	protected int description = R.string.item_unknown_description;
	protected boolean enabled = false;
	protected int icon = R.drawable.ui_funds_unknown;

	public int getName() {
		return name;
	}

	public int getDescription() {
		return description;
	}

	public void setEnabled(
			boolean isEnabled) {
		enabled = isEnabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
