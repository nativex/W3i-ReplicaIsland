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
		setImageDone(AchievementConstants.GODLIKE_IMAGE_EARNED);
		setImageLocked(AchievementConstants.GODLIKE_IMAGE_LOCKED);
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
