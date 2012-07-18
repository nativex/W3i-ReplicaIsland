package com.w3i.replica.replicaisland.achivements;

public class MegakillAchievement extends Achievement {

	public MegakillAchievement() {
		setName(AchievementConstants.MEGA_KILL_NAME);
		setDescription(AchievementConstants.MEGA_KILL_DESCRIPTION);
		setType(Type.MEGA_KILL);
		setLocked(true);
	}
}
