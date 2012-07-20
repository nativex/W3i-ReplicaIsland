package com.w3i.torch.achivements;

import com.google.gson.Gson;
import com.w3i.torch.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.SparseBooleanArray;

public class DiariesAchievement extends ProgressAchievement {
	private SparseBooleanArray diariesCollected;

	public DiariesAchievement() {
		super(AchievementConstants.DIARIES_GOAL);
		setName(AchievementConstants.DIARIES_NAME);
		setDescription(AchievementConstants.DIARIES_DESCRIPTION);
		setType(Type.DIARIES);
	}

	private void initializeDiaries() {
		if (diariesCollected == null) {
			diariesCollected = new SparseBooleanArray(15);
		}
		diariesCollected.clear();
		addDiaryEntry(R.string.Diary1);
		addDiaryEntry(R.string.Diary2);
		addDiaryEntry(R.string.Diary3);
		addDiaryEntry(R.string.Diary4);
		addDiaryEntry(R.string.Diary5);
		addDiaryEntry(R.string.Diary6);
		addDiaryEntry(R.string.Diary7);
		addDiaryEntry(R.string.Diary8);
		addDiaryEntry(R.string.Diary9);
		addDiaryEntry(R.string.Diary10);
		addDiaryEntry(R.string.Diary11);
		addDiaryEntry(R.string.Diary12);
		addDiaryEntry(R.string.Diary13);
		addDiaryEntry(R.string.Diary14);
		addDiaryEntry(R.string.Diary15);
	}

	public void onDiaryCollected(
			int diaryResourceIndex) {
		setDiaryCollected(diaryResourceIndex, true);
	}

	private void addDiaryEntry(
			int diaryResourceIndex) {
		setDiaryCollected(diaryResourceIndex, false);
	}

	private void setDiaryCollected(
			int diaryResourceIndex,
			boolean collected) {
		boolean alreadyCollected = diariesCollected.get(diaryResourceIndex);
		diariesCollected.put(diaryResourceIndex, collected);
		if ((collected) && (!alreadyCollected)) {
			increaseProgress(1);
			checkAchievementProgress();
		}
	}

	private void checkAchievementProgress() {
		for (int i = 0; i < diariesCollected.size(); i++) {
			if (!diariesCollected.valueAt(i)) {
				return;
			}
		}
		setDone(true);
	}

	private String getPreferencesDiariesDataString() {
		return getPreferencesName() + "DiariesCollected";
	}

	@Override
	public void storeAdditionalSharedPreferencesData(
			Editor editor) {

		String diariesData = new Gson().toJson(diariesCollected);
		editor.putString(getPreferencesDiariesDataString(), diariesData);
	}

	@Override
	public void loadAdditionalSharedPreferencesData(
			SharedPreferences preferences) {
		String diariesData = preferences.getString(getPreferencesDiariesDataString(), null);
		try {
			diariesCollected = new Gson().fromJson(diariesData, SparseBooleanArray.class);
		} catch (Exception e) {
		}
		if ((diariesCollected == null) || (diariesCollected.size() != 15)) {
			initializeDiaries();
		}
	}
}
