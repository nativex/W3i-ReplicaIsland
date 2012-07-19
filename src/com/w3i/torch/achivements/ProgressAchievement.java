package com.w3i.torch.achivements;

import com.w3i.common.Log;

public class ProgressAchievement extends Achievement {
	private int achievementProgress;
	private final int GOAL;
	private float achievementUpdateRate = DEFAULT_UPDATE_RATE;
	private int nextUpdate;

	private static final float DEFAULT_UPDATE_RATE = 25;

	protected ProgressAchievement(int goal) {
		GOAL = goal;
		setProgressAchievement(true);
		calculateNextUpdate();
	}

	private void calculateNextUpdate() {
		float updateIntervalFloat = (achievementUpdateRate * GOAL) / 100f + 0.5f;
		int updateInterval = (int) updateIntervalFloat;
		nextUpdate = ((achievementProgress / updateInterval) + 1) * updateInterval;
	}

	public void setProgress(
			int progress) {
		if (!isDone()) {
			Log.i("Achievement " + getName() + " (" + getDescription() + ") updated : " + progress + "/" + getGoal());
			this.achievementProgress = progress;
			if (progress > 0) {
				setLocked(false);
				if (progress < GOAL) {
					if (progress >= nextUpdate) {
						calculateNextUpdate();
						int percentDone = (progress * 100) / GOAL;
						AchievementManager.notifyAchievementProgressUpdated(this, percentDone);
					}
				}
			}
			if (progress >= GOAL) {
				setDone(true);
			}
		}
	}

	public int getProgress() {
		return achievementProgress > GOAL ? GOAL : achievementProgress;
	}

	public String getProgressString() {
		return convertProgress(getProgress()) + "/" + convertProgress(GOAL);
	}

	public int getGoal() {
		return GOAL;
	}

	public void increaseProgress(
			int increment) {
		setProgress(achievementProgress + increment);
	}

	protected void setAchievementUpdateRate(
			float newUpdateRate) {
		achievementUpdateRate = newUpdateRate;
		calculateNextUpdate();
	}

	protected String convertProgress(
			int i) {
		return Integer.toString(i);
	}
}
