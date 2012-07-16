package com.w3i.replica.replicaisland.achivements;

public class MultikillAchievement extends ProgressAchievement {

	public MultikillAchievement() {
		super(AchievementConstants.MULTI_KILL_GOAL);
		setName(AchievementConstants.MULTI_KILL_NAME);
		setDescription(AchievementConstants.MULTI_KILL_DESCRIPTION);
		setType(Type.MULTI_KILL);
	}

}
