package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class AdultsDifficultyAchievement extends Achievement {

	public AdultsDifficultyAchievement() {
		setName(R.string.achievement_adult_beat_name);
		setDescription(R.string.achievement_adult_beat_description);
		setType(Type.ADULT);
		setImageDone(R.drawable.ui_achievement_adult_beat);
		setImageLocked(R.drawable.ui_achievement_adult_beat_locked);
	}

}
