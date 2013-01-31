package com.recharge.torch.achivements;

import com.recharge.torch.R;


public class BonusCrystalsAchievement extends ProgressAchievement {

	public BonusCrystalsAchievement() {
		super(R.integer.achievement_bonus_crystals_goal);
		setName(R.string.achievement_bonus_crystals_name);
		setDescription(R.string.achievement_bonus_crystals_description);
		setType(Type.BONUS_CRYSTALS);
		setLocked(true);
		setImageLocked(R.drawable.ui_achievement_bonus_crystals_locked);
		setImageDone(R.drawable.ui_achievement_bonus_crystals);
	}
}
