package com.w3i.replica.replicaisland.achivements;

public class KillsAchievement extends ProgressAchievement {

	public KillsAchievement() {
		super(AchievementConstants.KILLS_ACHIEVEMENT_GOAL);
		setName(AchievementConstants.KILLS_ACHIEVEMENT_NAME);
		setDescription(AchievementConstants.KILLS_ACHIEVEMENT_DESCRIPTION);
		setType(Type.KILLS);
	}

}
