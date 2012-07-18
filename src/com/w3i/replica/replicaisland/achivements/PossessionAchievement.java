package com.w3i.replica.replicaisland.achivements;

public class PossessionAchievement extends ProgressAchievement {

	public PossessionAchievement() {
		super(AchievementConstants.POSSESSION_GOAL);
		setName(AchievementConstants.POSSESSION_NAME);
		setDescription(AchievementConstants.POSSESSION_DESCRIPTION);
		setType(Type.POSSESSION);
	}

}
