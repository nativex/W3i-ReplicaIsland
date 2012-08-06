package com.w3i.torch.achivements;

public class ShieldAchievement extends ProgressAchievement {

	public ShieldAchievement() {
		super(AchievementConstants.SHIELD_GOAL);
		setName(AchievementConstants.SHIELD_NAME);
		setDescription(AchievementConstants.SHIELD_DESCRIPTION);
		setType(Type.SHIELD);
		setImageDone(AchievementConstants.SHIELD_IMAGE_EARNED);
		setImageLocked(AchievementConstants.SHIELD_IMAGE_LOCKED);
	}

}
