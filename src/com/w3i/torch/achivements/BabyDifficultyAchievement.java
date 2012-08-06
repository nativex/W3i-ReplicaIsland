package com.w3i.torch.achivements;

public class BabyDifficultyAchievement extends Achievement {

	public BabyDifficultyAchievement() {
		setName(AchievementConstants.BABY_BEAT_NAME);
		setDescription(AchievementConstants.BABY_BEAT_DESCRIPTION);
		setType(Type.BABY);
		setImageLocked(AchievementConstants.BABY_BEAT_IMAGE_LOCKED);
		setImageDone(AchievementConstants.BABY_BEAT_IMAGE_EARNED);
	}
}
