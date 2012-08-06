package com.w3i.torch.achivements;

public class KabochaDefeated extends Achievement {

	public KabochaDefeated() {
		setName(AchievementConstants.KABOOCHA_NAME);
		setDescription(AchievementConstants.KABOOCHA_DESCRIPTION);
		setType(Type.KABOCHA_DEFEATED);
		setLocked(true);
		setImageDone(AchievementConstants.KABOOCHA_IMAGE_EARNED);
		setImageLocked(AchievementConstants.KABOOCHA_IMAGE_LOCKED);
	}
}
