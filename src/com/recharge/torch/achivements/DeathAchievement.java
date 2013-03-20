package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class DeathAchievement extends ProgressAchievement {

	public DeathAchievement() {
		super(R.integer.achievement_death_goal);
		setName(R.string.achievement_death_name);
		setDescription(R.string.achievement_death_description);
		setType(Type.DEATH);
		setImageLocked(R.drawable.ui_achievement_death_locked);
		setImageDone(R.drawable.ui_achievement_death);
	}
}
