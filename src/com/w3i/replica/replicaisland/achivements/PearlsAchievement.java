package com.w3i.replica.replicaisland.achivements;

public class PearlsAchievement extends ProgressAchievement {
	public static final int GOAL = 1000;

	public PearlsAchievement() {
		setName("Pearl Collector");
		setDescription("Collect 1000 pearls");
		setType(Type.PEARLS);
		setGoal(GOAL);
	}
}
