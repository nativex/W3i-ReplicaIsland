package com.w3i.torch.achivements;

import com.w3i.common.Log;

public class WindowShopperAchievement extends Achievement {
	private boolean failed = false;

	public WindowShopperAchievement() {
		setName(AchievementConstants.WINDOW_SHOPPER_NAME);
		setDescription(AchievementConstants.WINDOW_SHOPPER_DECRIPTION);
		setType(Type.WINDOW_SHOPPER);
		setLocked(true);
		setImageDone(AchievementConstants.WINDOW_SHOPPER_IMAGE_EARNED);
		setImageLocked(AchievementConstants.WINDOW_SHOPPER_IMAGE_LOCKED);
	}

	@Override
	public void onState(
			State state) {
		switch (state) {
		case START:
			failed = false;
			break;

		case FAIL:
			failed = true;
			break;

		case FINISH:
			if (!isDone()) {
				setDone(!failed);
			}
			break;

		}
		Log.d("WindowShopper state " + state.name() + " ; failed = " + failed);
	}
}
