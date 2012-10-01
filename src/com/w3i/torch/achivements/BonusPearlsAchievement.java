package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class BonusPearlsAchievement extends ProgressAchievement {

	public BonusPearlsAchievement() {
		super(R.integer.achievement_bonus_pearls_goal);
		setName(R.string.achievement_bonus_crystals_name);
		setDescription(R.string.achievement_bonus_crystals_description);
		setType(Type.BONUS_PEARLS);
		setLocked(true);
		setAchievementUpdateRate(12.5f);
	}

}
