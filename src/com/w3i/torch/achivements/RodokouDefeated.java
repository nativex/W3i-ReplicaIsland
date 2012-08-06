package com.w3i.torch.achivements;

public class RodokouDefeated extends Achievement {

	public RodokouDefeated() {
		setName(AchievementConstants.RODOKUO_NAME);
		setDescription(AchievementConstants.RODOKUO_DESCRIPTION);
		setType(Type.RODOKOU_DEFEATED);
		setLocked(true);
		setImageDone(AchievementConstants.RODOKUO_IMAGE_EARNED);
		setImageLocked(AchievementConstants.RODOKUO_IMAGE_LOCKED);
	}
}
