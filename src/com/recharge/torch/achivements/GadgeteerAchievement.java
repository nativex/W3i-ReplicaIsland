package com.recharge.torch.achivements;

import com.recharge.torch.R;
import com.recharge.torch.store.upgrades.Upgrades;

public class GadgeteerAchievement extends ProgressAchievement {

	public GadgeteerAchievement() {
		super(0);
		setName(R.string.achievement_gadgeteer_name);
		setDescription(R.string.achievement_gadgeteer_description);
		setType(Type.GADGETEER);
		setImageDone(R.drawable.ui_achievement_gadgeteer);
		setImageLocked(R.drawable.ui_achievement_gadgeteer_locked);
		setInitialized(false);
	}

	@Override
	public void setGoal(
			int goal) {
		super.setGoal(goal);
		if (goal > 0) {
			setInitialized(true);
		}
	}

	@Override
	public void onState(
			State state) {
		int owned = 0;
		for (Upgrades upgrade : Upgrades.values()) {
			if (upgrade.isOwned()) {
				owned++;
			}
		}
		setProgress(owned);
		setGoal(Upgrades.values().length);
	}
}
