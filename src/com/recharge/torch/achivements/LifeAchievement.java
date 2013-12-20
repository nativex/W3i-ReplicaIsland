package com.recharge.torch.achivements;

import com.recharge.torch.R;
import com.recharge.torch.store.upgrades.Upgrades;

public class LifeAchievement extends Achievement {

	public LifeAchievement() {
		setName(R.string.achievement_health_name);
		setDescription(R.string.achievement_health_description);
		setType(Type.HEALTH);
		setImageDone(R.drawable.ui_achievement_max_life);
		setImageLocked(R.drawable.ui_achievement_max_life_locked);
	}

	@Override
	public void onState(
			State state) {
		if (isDone()) {
			return;
		}
		if ((Upgrades.ALUMINIUM_PLATING.isOwned()) && (Upgrades.TITANIUM_PLATING.isOwned()) && (Upgrades.CARBON_PLATING.isOwned()) && (Upgrades.NANOBOTS_PLATING.isOwned())) {
			setDone(true);
		} else {
			setDone(false);
		}
	}
}
