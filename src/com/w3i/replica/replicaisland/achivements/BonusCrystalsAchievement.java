package com.w3i.replica.replicaisland.achivements;

public class BonusCrystalsAchievement extends ProgressAchievement {

	public BonusCrystalsAchievement() {
		super(AchievementConstants.BONUS_CRYSTALS_GOAL);
		setName(AchievementConstants.BONUS_CRYSTALS_NAME);
		setDescription(AchievementConstants.BONUS_CRYSTALS_DESCRIPTION);
		setType(Type.BONUS_CRYSTALS);
		setLocked(true);
	}
}
