package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class KillsAchievement extends ProgressAchievement {

	public KillsAchievement() {
		super(R.integer.achievement_kills_achievement_goal);
		setName(R.string.achievement_kills_achievement_name);
		setDescription(R.string.achievement_kills_achievement_description);
		setType(Type.KILLS);
		setImageDone(R.drawable.ui_achievement_destroyer);
		setImageLocked(R.drawable.ui_achievement_destroyer_locked);
	}

}
