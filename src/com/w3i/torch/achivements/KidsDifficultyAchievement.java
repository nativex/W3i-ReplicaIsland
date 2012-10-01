package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class KidsDifficultyAchievement extends Achievement {

	public KidsDifficultyAchievement() {
		setName(R.string.achievement_kids_beat_name);
		setDescription(R.string.achievement_kids_beat_description);
		setType(Type.KIDS);
		setImageDone(R.drawable.ui_achievement_kid_beat);
		setImageLocked(R.drawable.ui_achievement_kid_beat_locked);
	}
}
