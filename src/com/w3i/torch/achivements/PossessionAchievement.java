package com.w3i.torch.achivements;

public class PossessionAchievement extends ProgressAchievement {

	public PossessionAchievement() {
		super(AchievementConstants.POSSESSION_GOAL);
		setName(AchievementConstants.POSSESSION_NAME);
		setDescription(AchievementConstants.POSSESSION_DESCRIPTION);
		setType(Type.POSSESSION);
		setImageDone(AchievementConstants.POSSESSION_IMAGE_EARNED);
		setImageLocked(AchievementConstants.POSSESSION_IMAGE_LOCKED);
	}

}
