package com.w3i.torch.achivements;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.SparseBooleanArray;

import com.google.gson.Gson;
import com.w3i.torch.LevelTree;

public class DiariesAchievement extends ProgressAchievement {
	private SparseBooleanArray diariesCollected;

	public DiariesAchievement() {
		setName(AchievementConstants.DIARIES_NAME);
		setDescription(AchievementConstants.DIARIES_DESCRIPTION);
		setType(Type.DIARIES);
		setInitialized(false);
		initializeDiaryData();
	}

	private void initializeDiaryData() {
		diariesCollected = new SparseBooleanArray();
		ArrayList<LevelTree.LevelGroup> groups = LevelTree.levels;
		if ((groups != null) && (groups.size() > 0)) {
			for (LevelTree.LevelGroup group : groups) {
				ArrayList<LevelTree.Level> levels = group.levels;
				if ((levels != null) && (levels.size() > 0)) {
					for (LevelTree.Level level : levels) {
						if (level.dialogResources != null) {
							if (level.dialogResources.diaryEntry > 0) {
								addDiaryEntry(level.dialogResources.diaryEntry);
							}
						}
					}
				}
			}
		}
		setGoal(diariesCollected.size());
		setInitialized(true);
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
		diariesCollected.put(diaryResourceIndex, collected);
		setProgress(0);
	}

	@Override
	public void setProgress(
			int progress) {
		progress = 0;
		for (int i = 0; i < diariesCollected.size(); i++) {
			if (diariesCollected.valueAt(i)) {
				progress++;
			}
		}
		super.setProgress(progress);
	}

	private String getPreferencesDiariesDataString() {
		return getPreferencesName() + "DiariesCollected";
	}

	@Override
	public void storeSharedPreferencesData(
			Editor editor) {
		String diariesData = new Gson().toJson(diariesCollected);
		editor.putString(getPreferencesDiariesDataString(), diariesData);
		super.storeSharedPreferencesData(editor);
	}

	@Override
	public void loadSharedPreferencesData(
			SharedPreferences preferences) {
		String diariesData = preferences.getString(getPreferencesDiariesDataString(), null);
		try {
			diariesCollected = new Gson().fromJson(diariesData, SparseBooleanArray.class);
			setGoal(diariesCollected.size());
		} catch (Exception e) {
		}
		if ((diariesCollected == null) || (diariesCollected.size() == 0)) {
			initializeDiaryData();
		}
		super.loadSharedPreferencesData(preferences);
	}

	@Override
	public <T> void onState(
			State state,
			AchievementData<T> data) {
		T intData = data.getData();
		if (intData instanceof Integer) {
			switch (state) {
			case UPDATE:
				onDiaryCollected((Integer) intData);
				break;

			case INITIALIZE:
				initializeDiaryData();
				break;
			}
		}

	}

	@Override
	public void reset() {
		super.reset();
		initializeDiaryData();
	}

}
