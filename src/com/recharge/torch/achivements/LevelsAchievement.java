package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class LevelsAchievement extends ProgressAchievement {

	public LevelsAchievement() {
		super(R.integer.achievement_levels_goal);
		setName(R.string.achievement_levels_name);
		setDescription(R.string.achievement_levels_description);
		setType(Type.LEVELS);
		setImageDone(R.drawable.ui_achievement_good_job);
		setImageLocked(R.drawable.ui_achievement_good_job_locked);
	}

}
