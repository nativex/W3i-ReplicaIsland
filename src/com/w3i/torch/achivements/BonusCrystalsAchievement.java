package com.w3i.torch.achivements;

public class BonusCrystalsAchievement extends ProgressAchievement {

	public BonusCrystalsAchievement() {
		super(AchievementConstants.BONUS_CRYSTALS_GOAL);
		setName(AchievementConstants.BONUS_CRYSTALS_NAME);
		setDescription(AchievementConstants.BONUS_CRYSTALS_DESCRIPTION);
		setType(Type.BONUS_CRYSTALS);
		setLocked(true);
		setImageLocked(AchievementConstants.BONUS_CRYSTALS_IMAGE_LOCKED);
		setImageDone(AchievementConstants.BONUS_CRYSTALS_IMAGE_EARNED);
	}
}
