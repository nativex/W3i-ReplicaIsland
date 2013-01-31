package com.recharge.torch.achivements;

import com.recharge.torch.R;
import com.w3i.common.Log;

public class WindowShopperAchievement extends Achievement {
	private boolean failed = false;

	public WindowShopperAchievement() {
		setName(R.string.achievement_window_shopper_name);
		setDescription(R.string.achievement_window_shopper_decription);
		setType(Type.WINDOW_SHOPPER);
		setLocked(true);
		setImageDone(R.drawable.ui_achievement_window_shopper);
		setImageLocked(R.drawable.ui_achievement_window_shopper_locked);
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
