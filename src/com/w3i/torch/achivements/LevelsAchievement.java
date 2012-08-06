package com.w3i.torch.achivements;

public class LevelsAchievement extends ProgressAchievement {

	public LevelsAchievement() {
		super(AchievementConstants.LEVELS_GOAL);
		setName(AchievementConstants.LEVELS_NAME);
		setDescription(AchievementConstants.LEVELS_DESCRIPTION);
		setType(Type.LEVELS);
		setImageDone(AchievementConstants.LEVELS_IMAGE_EARNED);
		setImageLocked(AchievementConstants.LEVELS_IMAGE_LOCKED);
	}

}
