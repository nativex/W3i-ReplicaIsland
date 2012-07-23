package com.w3i.torch.achivements;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.SparseBooleanArray;

import com.google.gson.Gson;
import com.w3i.torch.LevelTree;

public class AllLevelsAchievement extends ProgressAchievement {
	private SparseBooleanArray levelsData;

	public AllLevelsAchievement() {
		super();
		setName(AchievementConstants.ALL_LEVELS_NAME);
		setDescription(AchievementConstants.ALL_LEVELS_DESCRIPTION);
		setType(Type.All_LEVELS);
		initializeLevelsData();
	}

	private void initializeLevelsData() {
		levelsData = new SparseBooleanArray();
		ArrayList<LevelTree.LevelGroup> groups = LevelTree.levels;
		if ((groups != null) && (groups.size() > 0)) {
			for (LevelTree.LevelGroup group : groups) {
				ArrayList<LevelTree.Level> levels = group.levels;
				if ((levels != null) && (levels.size() > 0)) {
					for (LevelTree.Level level : levels) {
						addLevel(level.resource, false);
					}
				}
			}
		}
		setGoal(levelsData.size());
	}

	private void addLevel(
			int levelId,
			boolean completed) {
		if (levelsData == null) {
			levelsData = new SparseBooleanArray();
		}
		levelsData.put(levelId, completed);
		setGoal(levelsData.size());
	}

	private void completeLevel(
			int levelId,
			boolean completed) {
		if (levelsData != null) {
			levelsData.put(levelId, completed);
			setProgress(0);
		}
	}

	@Override
	public void setProgress(
			int progress) {
		if (levelsData == null) {
			return;
		}
		progress = 0;
		for (int i = 0; i < levelsData.size(); i++) {
			if (levelsData.valueAt(i)) {
				progress++;
			}
		}
		super.setProgress(progress);
	}

	private String getPreferencesLevelsDataString() {
		return getPreferencesName() + "LevelsCompleted";
	}

	@Override
	public void loadAdditionalSharedPreferencesData(
			SharedPreferences preferences) {
		String json = preferences.getString(getPreferencesLevelsDataString(), null);
		try {
			levelsData = new Gson().fromJson(json, SparseBooleanArray.class);
			setGoal(levelsData.size());
		} catch (Exception e) {
		}
		if ((levelsData == null) || (levelsData.size() == 0)) {
			initializeLevelsData();
		}
	}

	@Override
	public void storeAdditionalSharedPreferencesData(
			Editor editor) {
		if (levelsData != null) {
			String json = new Gson().toJson(levelsData);
			editor.putString(getPreferencesLevelsDataString(), json);
			editor.commit();
		}

	}

	@Override
	public <T> void onState(
			State state,
			AchievementData<T> achievementData) {
		T data = achievementData.getData();
		if (data instanceof Integer) {

			switch (state) {
			case INITIALIZE:
				initializeLevelsData();
				break;

			case UPDATE:
				completeLevel((Integer) data, true);
				break;
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		initializeLevelsData();
	}

}
