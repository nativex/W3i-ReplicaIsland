package com.w3i.replica.replicaisland.achivements;

public class ProgressAchievement extends Achievement {
	private int progress;
	private int goal;

	public void setProgress(
			int progress) {
		this.progress = progress;
		if (progress >= goal) {
			setDone(true);
		}
	}

	protected void setGoal(
			int goal) {
		this.goal = goal;
	}

	public int getProgress() {
		return progress;
	}

	public int getGoal() {
		return goal;
	}
}
