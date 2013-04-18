package com.recharge.torch.achivements;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nativex.common.Log;
import com.recharge.torch.R;

public class GodlikeAchievement extends Achievement {
	private boolean failed = false;

	public GodlikeAchievement() {
		setName(R.string.achievement_godlike_name);
		setDescription(R.string.achievement_godlike_description);
		setType(Type.GODLIKE);
		setImageDone(R.drawable.ui_achievement_godlike);
		setImageLocked(R.drawable.ui_achievement_godlike_locked);
	}

	private String getPreferencesFailedKey() {
		return getPreferencesName() + "Failed";
	}

	@Override
	public void storeSharedPreferencesData(
			Editor editor) {
		editor.putBoolean(getPreferencesFailedKey(), failed);
		super.storeSharedPreferencesData(editor);
	}

	@Override
	public void loadSharedPreferencesData(
			SharedPreferences preferences) {
		failed = preferences.getBoolean(getPreferencesFailedKey(), true);
		super.loadSharedPreferencesData(preferences);
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
		Log.d("GodlikeAchievement state changed: State - " + state.name() + "; failed = " + failed);
	};
}