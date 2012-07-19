package com.w3i.torch.achivements;

public class DeathAchievement extends ProgressAchievement {

	public DeathAchievement() {
		super(AchievementConstants.DEATH_GOAL);
		setName(AchievementConstants.DEATH_NAME);
		setDescription(AchievementConstants.DEATH_DESCRIPTION);
		setType(Type.DEATH);
	}
}
