package com.w3i.replica.replicaisland.achivements;

public class CrystalsAchievement extends ProgressAchievement {

	public CrystalsAchievement() {
		super(AchievementConstants.CRYSTALS_GOAL);
		setName(AchievementConstants.CRYSTALS_NAME);
		setDescription(AchievementConstants.CRYSTALS_DESCRIPTION);
		setType(Type.CRYSTALS);
	}

}
