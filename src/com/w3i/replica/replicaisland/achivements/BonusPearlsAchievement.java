package com.w3i.replica.replicaisland.achivements;

public class BonusPearlsAchievement extends ProgressAchievement {

	public BonusPearlsAchievement() {
		super(AchievementConstants.BONUS_PEARLS_GOAL);
		setName(AchievementConstants.BONUS_PEARLS_NAME);
		setDescription(AchievementConstants.BONUS_PEARLS_DESCRIPTION);
		setType(Type.BONUS_PEARLS);		
	}
}
