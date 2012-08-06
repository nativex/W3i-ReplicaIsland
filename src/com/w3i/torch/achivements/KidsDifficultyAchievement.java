package com.w3i.torch.achivements;

public class KidsDifficultyAchievement extends Achievement {

	public KidsDifficultyAchievement() {
		setName(AchievementConstants.KIDS_BEAT_NAME);
		setDescription(AchievementConstants.KIDS_BEAT_DESCRIPTION);
		setType(Type.KIDS);
		setImageDone(AchievementConstants.KIDS_BEAT_IMAGE_EARNED);
		setImageLocked(AchievementConstants.KIDS_BEAT_IMAGE_LOCKED);
	}
}
