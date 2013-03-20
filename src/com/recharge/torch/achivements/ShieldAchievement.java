package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class ShieldAchievement extends ProgressAchievement {

	public ShieldAchievement() {
		super(R.integer.achievement_shield_goal);
		setName(R.string.achievement_shield_name);
		setDescription(R.string.achievement_shield_description);
		setType(Type.SHIELD);
		setImageDone(R.drawable.ui_achievement_shield);
		setImageLocked(R.drawable.ui_achievement_shield_locked);
	}

}
