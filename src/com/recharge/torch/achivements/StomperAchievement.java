package com.recharge.torch.achivements;

import com.recharge.torch.R;


public class StomperAchievement extends ProgressAchievement {

	public StomperAchievement() {
		super(R.integer.achievement_stomper_goal);
		setName(R.string.achievement_stomper_name);
		setDescription(R.string.achievement_stomper_description);
		setType(Type.STOMP);
		setImageDone(R.drawable.ui_achievement_stomper);
		setImageLocked(R.drawable.ui_achievement_stomper_locked);
	}

}
