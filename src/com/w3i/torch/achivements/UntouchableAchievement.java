package com.w3i.torch.achivements;

import com.w3i.common.Log;
import com.w3i.torch.R;

public class UntouchableAchievement extends Achievement {

	private boolean achievementFailed = false;

	public UntouchableAchievement() {
		setName(R.string.achievement_untouchable_name);
		setDescription(R.string.achievement_untouchable_description);
		setType(Type.UNTOUCHABLE);
		setImageDone(R.drawable.ui_achievement_untouchable);
		setImageLocked(R.drawable.ui_achievement_untouchable_locked);
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
