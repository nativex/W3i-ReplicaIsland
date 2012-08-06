package com.w3i.torch.achivements;

public class MegakillAchievement extends Achievement {

	public MegakillAchievement() {
		setName(AchievementConstants.MEGA_KILL_NAME);
		setDescription(AchievementConstants.MEGA_KILL_DESCRIPTION);
		setType(Type.MEGA_KILL);
		setLocked(true);
		setImageDone(AchievementConstants.MEGA_KILL_IMAGE_EARNED);
		setImageLocked(AchievementConstants.MEGA_KILL_IMAGE_LOCKED);
	}
}
