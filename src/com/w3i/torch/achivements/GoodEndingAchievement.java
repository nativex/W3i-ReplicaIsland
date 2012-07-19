package com.w3i.torch.achivements;

public class GoodEndingAchievement extends Achievement {

	public GoodEndingAchievement() {
		setType(Type.GOOD_ENDING);
		setName(AchievementConstants.GOOD_BOY_NAME);
		setDescription(AchievementConstants.GOOD_BOY_DESCRIPTION);
		setLocked(true);
	}
}
