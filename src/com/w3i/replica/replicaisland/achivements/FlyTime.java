package com.w3i.replica.replicaisland.achivements;

import com.w3i.replica.replicaisland.utils.TimeUtils;

public class FlyTime extends ProgressAchievement {
	private double flyTime = 0;
	private boolean flying = false;

	public FlyTime() {
		super(AchievementConstants.FLY_TIME_GOAL);
		setName(AchievementConstants.FLY_TIME_NAME);
		setDescription(AchievementConstants.FLY_TIME_DESCRIPTION);
		setType(Type.FLY_TIME);
	}

	public void startedFlaying() {
		flying = true;
		flyTime = 0;
	}

	public void updateFlying(
			double delta) {
		flyTime += delta;
	}

	public boolean isFlying() {
		return flying;
	}

	public void endFlying() {
		flying = false;
		increaseProgress((int) (flyTime + 0.5));
	}

	@Override
	protected String convertProgress(
			int progress) {
		return TimeUtils.convertSecondsToString(progress);

	}

}
