package com.w3i.torch.achivements;

import com.w3i.common.Log;

public class WindowShopper extends Achievement {
	private boolean failed = false;

	public WindowShopper() {
		setName(AchievementConstants.WINDOW_SHOPPER_NAME);
		setDescription(AchievementConstants.WINDOW_SHOPPER_DECRIPTION);
		setType(Type.WINDOW_SHOPPER);
		setLocked(true);
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
			setDone(!failed);
			break;

		}
		Log.d("WindowShopper state " + state.name() + " ; failed = " + failed);
	}
}
