package com.w3i.replica.replicaisland.achivements;

public class FlyTime extends ProgressAchievement {
	private double flyTime = 0;
	private boolean flying = false;

	public FlyTime() {
		super(AchievementConstants.FLY_TIME_GOAL);
		setName(AchievementConstants.FLY_TIME_NAME);
		setDescription(AchievementConstants.FLY_TIME_DESCRIPTION);
		setType(Type.FLY_TIME);
	}

	public void startedFlaying() {
		flying = true;
		flyTime = 0;
	}

	public void updateFlying(
			double delta) {
		flyTime += delta;
	}

	public boolean isFlying() {
		return flying;
	}

	public void endFlying() {
		flying = false;
		increaseProgress((int) (flyTime + 0.5));
	}

	@Override
	protected String convertProgress(
			int progress) {
		int seconds = progress % 60;
		int minutes = progress / 60;

		String time = (minutes < 10 ? "0" + minutes : Integer.toString(minutes)) + ":";
		time += (seconds < 10 ? "0" + seconds : Integer.toString(seconds));
		return time;

	}

}
