package com.w3i.torch.achivements;

public class MultikillAchievement extends ProgressAchievement {

	public MultikillAchievement() {
		super(AchievementConstants.MULTI_KILL_GOAL);
		setName(AchievementConstants.MULTI_KILL_NAME);
		setDescription(AchievementConstants.MULTI_KILL_DESCRIPTION);
		setType(Type.MULTI_KILL);
		setLocked(true);
		setImageDone(AchievementConstants.MULTI_KILL_IMAGE_EARNED);
		setImageLocked(AchievementConstants.MULTI_KILL_IMAGE_LOCKED);
	}

}
