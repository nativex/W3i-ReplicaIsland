package com.w3i.replica.replicaisland.achivements;

import com.w3i.replica.replicaisland.utils.TimeUtils;

public class AchievementConstants {
	// Crystal Collector
	public static final int CRYSTALS_GOAL = 150;
	public static final String CRYSTALS_NAME = "Crystal Collector";
	public static final String CRYSTALS_DESCRIPTION = "Collect " + CRYSTALS_GOAL + " crystals.";

	// Pearls Collector
	public static final int PEARLS_GOAL = 1000;
	public static final String PEARLS_NAME = "Pearl Collector";
	public static final String PEARLS_DESCRIPTION = "Collect " + PEARLS_GOAL + " pearls.";

	// Fly Time
	public static final int FLY_TIME_GOAL = 3600;
	public static final String FLY_TIME_NAME = "Flier";
	public static final String FLY_TIME_DESCRIPTION = "Spend " + TimeUtils.getTimeAchievementStringFromSeconds(FLY_TIME_GOAL) + " in the air.";

	// Jetpack Time
	public static final int JETPACK_TIME_GOAL = 1200;
	public static final String JETPACK_TIME_NAME = "Jetpack Ace";
	public static final String JETPACK_TIME_DESCRIPTION = "Spend " + TimeUtils.getTimeAchievementStringFromSeconds(JETPACK_TIME_GOAL) + " using the jetpack.";

	// Good Boy
	public static final String GOOD_BOY_NAME = "Good Boy!";
	public static final String GOOD_BOY_DESCRIPTION = "Beat the game with a good ending.";

	// Kills Achievement
	public static final int KILLS_ACHIEVEMENT_GOAL = 250;
	public static final String KILLS_ACHIEVEMENT_NAME = "Destroyer";
	public static final String KILLS_ACHIEVEMENT_DESCRIPTION = "Destroy " + KILLS_ACHIEVEMENT_GOAL + " monsters.";

	// Multi Kill
	public static final int MULTI_KILL_GOAL = 100;
	public static final String MULTI_KILL_NAME = "Multikill";
	public static final String MULTI_KILL_DESCRIPTION = "Do " + MULTI_KILL_GOAL + " killing sprees.";

	// Mega Kill
	public static final int MEGA_KILL_GOAL = 4;
	public static final String MEGA_KILL_NAME = "Megakill";
	public static final String MEGA_KILL_DESCRIPTION = "Destroy " + MEGA_KILL_GOAL + " enemies in a killing spree.";

	// Levels Achievement
	public static final int LEVELS_GOAL = 50;
	public static final String LEVELS_NAME = "Good job";
	public static final String LEVELS_DESCRIPTION = "Finish " + LEVELS_GOAL + " levels.";

	// Max Hit Points
	public static final String HEALTH_NAME = "Max Life";
	public static final String HEALTH_DESCRIPTION = "Max your life";

	// Pearls Bonus
	public static final int BONUS_PEARLS_GOAL = 2000;
	public static final String BONUS_PEARLS_NAME = "Bonus Pearls";
	public static final String BONUS_PEARLS_DESCRIPTION = "Generate " + BONUS_PEARLS_GOAL + " pearls with the Garbage Collector";

	// Crystals Bonus
	public static final int BONUS_CRYSTALS_GOAL = 250;
	public static final String BONUS_CRYSTALS_NAME = "Bonus Crystals";
	public static final String BONUS_CRYSTALS_DESCRIPTION = "Generate " + BONUS_CRYSTALS_GOAL + " crystals with the Garbage Collector.";

}
