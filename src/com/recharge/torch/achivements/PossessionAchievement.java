package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class PossessionAchievement extends ProgressAchievement {

	public PossessionAchievement() {
		super(R.integer.achievement_possession_goal);
		setName(R.string.achievement_possession_name);
		setDescription(R.string.achievement_possession_description);
		setType(Type.POSSESSION);
		setImageDone(R.drawable.ui_achievement_possession);
		setImageLocked(R.drawable.ui_achievement_possession_locked);
	}

}
