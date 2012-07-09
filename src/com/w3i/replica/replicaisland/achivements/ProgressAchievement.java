package com.w3i.replica.replicaisland.achivements;

public class ProgressAchievement extends Achievement {
	private int achievementProgress;
	private final int GOAL;

	protected ProgressAchievement(int goal) {
		GOAL = goal;
		setProgressAchievement(true);
	}

	public void setProgress(
			int progress) {
		if (!isDone()) {
			this.achievementProgress = progress;
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

	protected String convertProgress(
			int i) {
		return Integer.toString(i);
	}
}
