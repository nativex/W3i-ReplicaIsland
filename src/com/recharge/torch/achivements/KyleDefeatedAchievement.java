package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class KyleDefeatedAchievement extends Achievement {

	public KyleDefeatedAchievement() {
		setName(R.string.achievement_kyle_name);
		setDescription(R.string.achievement_kyle_description);
		setType(Type.KYLE_DEFEATED);
		setLocked(true);
		setImageDone(R.drawable.ui_achievement_kyle);
		setImageLocked(R.drawable.ui_achievement_kyle_locked);
	}
}
