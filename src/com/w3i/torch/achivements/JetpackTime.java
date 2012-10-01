package com.w3i.torch.achivements;

import android.content.Context;

import com.w3i.common.Log;
import com.w3i.torch.R;
import com.w3i.torch.utils.TimeUtils;

public class JetpackTime extends ProgressAchievement {
	private double flyTime = 0;
	private boolean flying = false;

	public JetpackTime() {
		super(R.integer.achievement_jetpack_time_goal);
		setName(R.string.achievement_jetpack_time_name);
		setDescription(R.string.achievement_jetpack_time_description);
		setType(Type.JETPACK_TIME);
		setImageDone(R.drawable.ui_achievement_jetpack_ace);
		setImageLocked(R.drawable.ui_achievement_jetpack_ace_locked);
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

	@Override
	public String formatText(
			Context context,
			int text) {
		String unformattedText = context.getResources().getString(text);
		String formattedText = unformattedText.replace("#", TimeUtils.getTimeAchievementStringFromSeconds(getGoal()));
		return formattedText;
	}
}
