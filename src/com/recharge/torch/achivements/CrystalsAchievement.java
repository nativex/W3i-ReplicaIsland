package com.recharge.torch.achivements;

import com.recharge.torch.R;


public class CrystalsAchievement extends ProgressAchievement {

	public CrystalsAchievement() {
		super(R.integer.achievement_crystals_goal);
		setName(R.string.achievement_crystals_name);
		setDescription(R.string.achievement_crystals_description);
		setType(Type.CRYSTALS);
		setImageDone(R.drawable.ui_achievement_crystals);
		setImageLocked(R.drawable.ui_achievement_crystals_locked);
	}
}
