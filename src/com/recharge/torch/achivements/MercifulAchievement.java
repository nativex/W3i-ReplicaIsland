package com.recharge.torch.achivements;

import com.recharge.torch.R;
import com.w3i.common.Log;

public class MercifulAchievement extends Achievement {
	private boolean achievementFailed = false;

	public MercifulAchievement() {
		setName(R.string.achievement_merciful_name);
		setDescription(R.string.achievement_merciful_description);
		setType(Type.MERCIFUL);
		setImageDone(R.drawable.ui_achievement_merciful);
		setImageLocked(R.drawable.ui_achievement_merciful_locked);
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
