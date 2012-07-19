package com.w3i.torch.achivements;

public class BonusPearlsAchievement extends ProgressAchievement {

	public BonusPearlsAchievement() {
		super(AchievementConstants.BONUS_PEARLS_GOAL);
		setName(AchievementConstants.BONUS_PEARLS_NAME);
		setDescription(AchievementConstants.BONUS_PEARLS_DESCRIPTION);
		setType(Type.BONUS_PEARLS);
		setLocked(true);
		setAchievementUpdateRate(12.5f);
	}

	private boolean test = true;

	@Override
	public void setProgress(
			int progress) {
		if (test) {
			progress = 240;
			test = false;
		}
		super.setProgress(progress);

	}
}
