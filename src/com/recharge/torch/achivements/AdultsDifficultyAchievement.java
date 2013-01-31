package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class AdultsDifficultyAchievement extends Achievement {

	public AdultsDifficultyAchievement() {
		setName(R.string.achievement_adult_beat_name);
		setDescription(R.string.achievement_adult_beat_description);
		setType(Type.ADULT);
		setImageDone(R.drawable.ui_achievement_adult_beat);
		setImageLocked(R.drawable.ui_achievement_adult_beat_locked);
	}

}
