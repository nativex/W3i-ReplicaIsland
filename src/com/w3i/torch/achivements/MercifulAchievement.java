package com.w3i.torch.achivements;

import com.w3i.common.Log;

public class MercifulAchievement extends Achievement {
	private boolean achievementFailed = false;

	public MercifulAchievement() {
		setName(AchievementConstants.MERCIFUL_NAME);
		setDescription(AchievementConstants.MERCIFUL_DESCRIPTION);
		setType(Type.MERCIFUL);
	}

	private void levelFinished() {
		if ((!achievementFailed) && (!isDone())) {
			Log.d("MercifulAchievement done.");
			setDone(true);
		}
	}

	@Override
	public void onState(
			State state) {
		switch (state) {
		case FAIL:
			achievementFailed = true;
			Log.d("MercifulAchievement failed.");
			break;
		case FINISH:
			levelFinished();
			break;
		case START:
			achievementFailed = false;
			Log.d("MercifulAchievement reset.");
			break;
		}
	}
}
