package com.recharge.torch.achivements;

import com.recharge.torch.R;
import com.recharge.torch.utils.TimeUtils;
import com.w3i.common.Log;

public class FlyTime extends ProgressAchievement {
	private double flyTime = 0;
	private boolean flying = false;

	public FlyTime() {
		super(R.integer.achievement_fly_time_goal);
		setName(R.string.achievement_fly_time_name);
		setDescription(R.string.achievement_fly_time_description);
		setType(Type.FLY_TIME);
		setImageLocked(R.drawable.ui_achievement_flier_locked);
		setImageDone(R.drawable.ui_achievement_flier);
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

	@Override
	protected String makeDescriptionReplacement(
			String description) {
		return description.replace("#", TimeUtils.getTimeAchievementStringFromSeconds(getGoal()));
	}
}
