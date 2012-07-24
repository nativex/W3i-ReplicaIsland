package com.w3i.torch.achivements;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.w3i.common.Log;

public class GodlikeAchievement extends Achievement {
	private boolean failed = false;

	public GodlikeAchievement() {
		setName(AchievementConstants.GODLIKE_NAME);
		setDescription(AchievementConstants.GODLIKE_DESCRIPTION);
		setType(Type.GODLIKE);
	}

	private String getPreferencesFailedKey() {
		return getPreferencesName() + "Failed";
	}

	@Override
	public void storeAdditionalSharedPreferencesData(
			Editor editor) {
		editor.putBoolean(getPreferencesFailedKey(), failed);
	}

	@Override
	public void loadAdditionalSharedPreferencesData(
			SharedPreferences preferences) {
		failed = preferences.getBoolean(getPreferencesFailedKey(), true);
	}

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
