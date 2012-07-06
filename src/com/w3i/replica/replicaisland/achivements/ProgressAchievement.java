package com.w3i.replica.replicaisland.achivements;

public class ProgressAchievement extends Achievement {
	private int progress;
	private final int GOAL;

	protected ProgressAchievement(int goal) {
		GOAL = goal;
	}

	public void setProgress(
			int progress) {
		this.progress = progress;
		if (progress >= GOAL) {
			setDone(true);
		}
	}

	public int getProgress() {
		return progress;
	}

	public int getGoal() {
		return GOAL;
	}
}
