package com.recharge.torch.general;

import com.recharge.torch.R;

public enum StoreCategory {
	LIFE(R.string.category_life_name),
	JETPACK(R.string.category_jetpack_name),
	SHIELD(R.string.category_shield_name),
	MISC(R.string.category_misc_name);

	private int name;

	private StoreCategory(int n) {
		name = n;
	}

	public int getName() {
		return name;
	}
}
