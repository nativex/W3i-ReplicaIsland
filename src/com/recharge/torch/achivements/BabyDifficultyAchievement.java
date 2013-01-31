package com.recharge.torch.achivements;

import com.recharge.torch.R;


public class BabyDifficultyAchievement extends Achievement {

	public BabyDifficultyAchievement() {
		setName(R.string.achievement_baby_beat_name);
		setDescription(R.string.achievement_baby_beat_description);
		setType(Type.BABY);
		setImageLocked(R.drawable.ui_achievement_baby_beat_locked);
		setImageDone(R.drawable.ui_achievement_baby_beat);
	}
}
