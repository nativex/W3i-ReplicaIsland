package com.w3i.torch.achivements;

import com.w3i.common.Log;

public class UntouchableAchievement extends Achievement {

	private boolean achievementFailed = false;

	public UntouchableAchievement() {
		setName(AchievementConstants.UNTOUCHABLE_NAME);
		setDescription(AchievementConstants.UNTOUCHABLE_DESCRIPTION);
		setType(Type.UNTOUCHABLE);
		setImageDone(AchievementConstants.UNTOUCHABLE_IMAGE_EARNED);
		setImageLocked(AchievementConstants.UNTOUCHABLE_IMAGE_LOCKED);
	}

	private void levelFinished() {
		if ((!achievementFailed) && (!isDone())) {
			Log.d("UntouchableAchievement done.");
			setDone(true);
		}
	}

	@Override
	public void onState(
			State state) {
		switch (state) {
		case FAIL:
			achievementFailed = true;
			Log.d("UntouchableAchievement failed.");
			break;
		case FINISH:
			levelFinished();
			break;
		case START:
			achievementFailed = false;
			Log.d("UntouchableAchievement reset.");
			break;
		}
	}
}
