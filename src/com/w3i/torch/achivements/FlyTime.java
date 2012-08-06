package com.w3i.torch.achivements;

import com.w3i.common.Log;
import com.w3i.torch.utils.TimeUtils;

public class FlyTime extends ProgressAchievement {
	private double flyTime = 0;
	private boolean flying = false;

	public FlyTime() {
		super(AchievementConstants.FLY_TIME_GOAL);
		setName(AchievementConstants.FLY_TIME_NAME);
		setDescription(AchievementConstants.FLY_TIME_DESCRIPTION);
		setType(Type.FLY_TIME);
		setImageLocked(AchievementConstants.FLY_TIME_IMAGE_LOCKED);
		setImageDone(AchievementConstants.FLY_TIME_IMAGE_EARNED);
	}

	public void startedFlaying() {
		flying = true;
		flyTime = 0;
	}

	public void updateFlying(
			double delta) {
		flyTime += delta;
		Log.d("FlyTime: Flown " + flyTime);
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
