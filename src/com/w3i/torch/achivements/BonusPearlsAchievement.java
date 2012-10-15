package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class BonusPearlsAchievement extends ProgressAchievement {

	public BonusPearlsAchievement() {
		super(R.integer.achievement_bonus_pearls_goal);
		setName(R.string.achievement_bonus_pearls_name);
		setDescription(R.string.achievement_bonus_pearls_description);
		setType(Type.BONUS_PEARLS);
		setLocked(true);
		setAchievementUpdateRate(12.5f);
		setImageDone(R.drawable.ui_achievement_bonus_pearls);
		setImageLocked(R.drawable.ui_achievement_bonus_pearls_locked);
	}

}
