package com.w3i.torch.achivements;

public class BonusPearlsAchievement extends ProgressAchievement {

	public BonusPearlsAchievement() {
		super(AchievementConstants.BONUS_PEARLS_GOAL);
		setName(AchievementConstants.BONUS_PEARLS_NAME);
		setDescription(AchievementConstants.BONUS_PEARLS_DESCRIPTION);
		setType(Type.BONUS_PEARLS);
		setLocked(true);
		setAchievementUpdateRate(12.5f);
		setImageDone(AchievementConstants.BONUS_PEARLS_IMAGE_EARNED);
		setImageLocked(AchievementConstants.BONUS_PEARLS_IMAGE_LOCKED);
	}

}
