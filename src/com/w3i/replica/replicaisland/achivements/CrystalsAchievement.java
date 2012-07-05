package com.w3i.replica.replicaisland.achivements;

public class CrystalsAchievement extends ProgressAchievement {
	public static final int GOAL = 200;

	public CrystalsAchievement() {
		setName("Crystal Collector");
		setDescription("Collect 200 crystals");
		setType(Type.CRYSTALS);
		setProgress(true);
		setGoal(GOAL);
	}

}
