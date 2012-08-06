package com.w3i.torch.achivements;

public class GameBeatAchievement extends Achievement {

	public GameBeatAchievement() {
		setName(AchievementConstants.GAME_BEAT_NAME);
		setDescription(AchievementConstants.GAME_BEAT_DESCRIPTION);
		setType(Type.GAME_BEAT);
		setImageDone(AchievementConstants.GAME_BEAT_IMAGE_EARNED);
		setImageLocked(AchievementConstants.GAME_BEAT_IMAGE_LOCKED);
	}
}
