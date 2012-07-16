package com.w3i.replica.replicaisland.achivements;

public class LifeAchievement extends Achievement{
	
	public LifeAchievement() {
		setName(AchievementConstants.HEALTH_NAME);
		setDescription(AchievementConstants.HEALTH_DESCRIPTION);
		setType(Type.HEALTH);
	}
}
