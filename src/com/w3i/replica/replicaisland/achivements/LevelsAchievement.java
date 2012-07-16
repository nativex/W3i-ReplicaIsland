package com.w3i.replica.replicaisland.achivements;

public class LevelsAchievement extends ProgressAchievement {

	public LevelsAchievement() {
		super(AchievementConstants.LEVELS_GOAL);
		setName(AchievementConstants.LEVELS_NAME);
		setDescription(AchievementConstants.LEVELS_DESCRIPTION);
		setType(Type.LEVELS);
	}

}
