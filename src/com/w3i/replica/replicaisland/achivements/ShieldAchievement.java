package com.w3i.replica.replicaisland.achivements;

public class ShieldAchievement extends ProgressAchievement {

	public ShieldAchievement() {
		super(AchievementConstants.SHIELD_GOAL);
		setName(AchievementConstants.SHIELD_NAME);
		setDescription(AchievementConstants.SHIELD_DESCRIPTION);
		setType(Type.SHIELD);
	}

}
