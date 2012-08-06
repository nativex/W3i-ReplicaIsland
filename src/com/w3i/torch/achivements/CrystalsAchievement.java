package com.w3i.torch.achivements;

public class CrystalsAchievement extends ProgressAchievement {

	public CrystalsAchievement() {
		super(AchievementConstants.CRYSTALS_GOAL);
		setName(AchievementConstants.CRYSTALS_NAME);
		setDescription(AchievementConstants.CRYSTALS_DESCRIPTION);
		setType(Type.CRYSTALS);
		setImageDone(AchievementConstants.CRYSTALS_IMAGE_EARNED);
		setImageLocked(AchievementConstants.CRYSTALS_IMAGE_LOCKED);
	}

}
