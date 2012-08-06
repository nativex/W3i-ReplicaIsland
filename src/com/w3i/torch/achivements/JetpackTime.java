package com.w3i.torch.achivements;

import com.w3i.common.Log;
import com.w3i.torch.utils.TimeUtils;

public class JetpackTime extends ProgressAchievement {
	private double flyTime = 0;
	private boolean flying = false;

	public JetpackTime() {
		super(AchievementConstants.JETPACK_TIME_GOAL);
		setName(AchievementConstants.JETPACK_TIME_NAME);
		setDescription(AchievementConstants.JETPACK_TIME_DESCRIPTION);
		setType(Type.JETPACK_TIME);
		setImageDone(AchievementConstants.JETPACK_TIME_IMAGE_EARNED);
		setImageLocked(AchievementConstants.JETPACK_TIME_IMAGE_LOCKED);
	}

	public void startedFlaying() {
		flying = true;
		flyTime = 0;
	}

	public void updateFlying(
			double delta) {
		flyTime += delta;
		Log.d("JetpackTime: Flown " + flyTime);
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
