package com.w3i.torch.achivements;

import com.w3i.torch.R;
import com.w3i.torch.utils.TimeUtils;

public class AchievementConstants {
	// Crystal Collector
	public static final int CRYSTALS_GOAL = 150;
	public static final String CRYSTALS_NAME = "Crystal Collector";
	public static final String CRYSTALS_DESCRIPTION = "Collect " + CRYSTALS_GOAL + " crystals.";
	public static final int CRYSTALS_IMAGE_LOCKED = R.drawable.ui_achievement_crystal_collector_locked;
	public static final int CRYSTALS_IMAGE_EARNED = R.drawable.ui_achievement_crystal_collector;

	// Pearls Collector
	public static final int PEARLS_GOAL = 1000;
	public static final String PEARLS_NAME = "Pearl Collector";
	public static final String PEARLS_DESCRIPTION = "Collect " + PEARLS_GOAL + " pearls.";
	public static final int PEARLS_IMAGE_LOCKED = R.drawable.ui_achievement_pearl_collector_locked;
	public static final int PEARLS_IMAGE_EARNED = R.drawable.ui_achievement_pearl_collector;

	// Fly Time
	public static final int FLY_TIME_GOAL = 3600;
	public static final String FLY_TIME_NAME = "Flier";
	public static final String FLY_TIME_DESCRIPTION = "Spend " + TimeUtils.getTimeAchievementStringFromSeconds(FLY_TIME_GOAL) + " in the air.";
	public static final int FLY_TIME_IMAGE_LOCKED = R.drawable.ui_achievement_flier_locked;
	public static final int FLY_TIME_IMAGE_EARNED = R.drawable.ui_achievement_flier;

	// Jetpack Time
	public static final int JETPACK_TIME_GOAL = 1200;
	public static final String JETPACK_TIME_NAME = "Jetpack Ace";
	public static final String JETPACK_TIME_DESCRIPTION = "Spend " + TimeUtils.getTimeAchievementStringFromSeconds(JETPACK_TIME_GOAL) + " using the jetpack.";
	public static final int JETPACK_TIME_IMAGE_LOCKED = R.drawable.ui_achievement_jetpack_ace_locked;
	public static final int JETPACK_TIME_IMAGE_EARNED = R.drawable.ui_achievement_jetpack_ace;

	// Good Boy
	// TODO
	public static final String GOOD_BOY_NAME = "Good Boy!";
	public static final String GOOD_BOY_DESCRIPTION = "Beat the game with a good ending.";
	public static final int GOOD_BOY_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int GOOD_BOY_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Kills Achievement
	public static final int KILLS_ACHIEVEMENT_GOAL = 250;
	public static final String KILLS_ACHIEVEMENT_NAME = "Destroyer";
	public static final String KILLS_ACHIEVEMENT_DESCRIPTION = "Destroy " + KILLS_ACHIEVEMENT_GOAL + " monsters.";
	public static final int KILLS_IMAGE_LOCKED = R.drawable.ui_achievement_destroyer_locked;
	public static final int KILLS_IMAGE_EARNED = R.drawable.ui_achievement_destroyer;

	// Multi Kill
	public static final int MULTI_KILL_GOAL = 100;
	public static final String MULTI_KILL_NAME = "Multikill";
	public static final String MULTI_KILL_DESCRIPTION = "Do " + MULTI_KILL_GOAL + " killing sprees.";
	public static final int MULTI_KILL_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int MULTI_KILL_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Mega Kill
	public static final int MEGA_KILL_GOAL = 4;
	public static final String MEGA_KILL_NAME = "Megakill";
	public static final String MEGA_KILL_DESCRIPTION = "Destroy " + MEGA_KILL_GOAL + " enemies in a killing spree.";
	public static final int MEGA_KILL_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int MEGA_KILL_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Levels Achievement
	public static final int LEVELS_GOAL = 50;
	public static final String LEVELS_NAME = "Good job";
	public static final String LEVELS_DESCRIPTION = "Finish " + LEVELS_GOAL + " levels.";
	public static final int LEVELS_IMAGE_LOCKED = R.drawable.ui_achievement_good_job_locked;
	public static final int LEVELS_IMAGE_EARNED = R.drawable.ui_achievement_good_job;

	// Max Hit Points
	public static final String HEALTH_NAME = "Max Life";
	public static final String HEALTH_DESCRIPTION = "Max your life";
	public static final int HEALTH_IMAGE_LOCKED = R.drawable.ui_achievement_max_life_locked;
	public static final int HEALTH_IMAGE_EARNED = R.drawable.ui_achievement_max_life;

	// Pearls Bonus
	public static final int BONUS_PEARLS_GOAL = 2000;
	public static final String BONUS_PEARLS_NAME = "Bonus Pearls";
	public static final String BONUS_PEARLS_DESCRIPTION = "Generate " + BONUS_PEARLS_GOAL + " pearls with the Garbage Collector";
	public static final int BONUS_PEARLS_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int BONUS_PEARLS_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Crystals Bonus
	public static final int BONUS_CRYSTALS_GOAL = 250;
	public static final String BONUS_CRYSTALS_NAME = "Bonus Crystals";
	public static final String BONUS_CRYSTALS_DESCRIPTION = "Generate " + BONUS_CRYSTALS_GOAL + " crystals with the Garbage Collector.";
	public static final int BONUS_CRYSTALS_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int BONUS_CRYSTALS_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Death
	public static final int DEATH_GOAL = 25;
	public static final String DEATH_NAME = "Death";
	public static final String DEATH_DESCRIPTION = "Die " + DEATH_GOAL + " times.";
	public static final int DEATH_IMAGE_LOCKED = R.drawable.ui_achievement_death_locked;
	public static final int DEATH_IMAGE_EARNED = R.drawable.ui_achievement_death;

	// Hit
	public static final int HIT_GOAL = 150;
	public static final String HIT_NAME = "HIT";
	public static final String HIT_DESCRIPTION = "Get hit " + HIT_GOAL + " times.";
	public static final int HIT_IMAGE_LOCKED = R.drawable.ui_achievement_hit_locked;
	public static final int HIT_IMAGE_EARNED = R.drawable.ui_achievement_hit;

	// Shield
	public static final int SHIELD_GOAL = 100;
	public static final String SHIELD_NAME = "Shield";
	public static final String SHIELD_DESCRIPTION = "Activate shield " + SHIELD_GOAL + " times.";
	public static final int SHIELD_IMAGE_LOCKED = R.drawable.ui_achievement_shield_locked;
	public static final int SHIELD_IMAGE_EARNED = R.drawable.ui_achievement_shield;

	// Possession
	public static final int POSSESSION_GOAL = 100;
	public static final String POSSESSION_NAME = "Possession";
	public static final String POSSESSION_DESCRIPTION = "Gain control over " + POSSESSION_GOAL + " robots.";
	public static final int POSSESSION_IMAGE_LOCKED = R.drawable.ui_achievement_possession_locked;
	public static final int POSSESSION_IMAGE_EARNED = R.drawable.ui_achievement_possession;

	// Kyle
	public static final String KYLE_NAME = "Kyle";
	public static final String KYLE_DESCRIPTION = "Defeat Kyle.";
	public static final int KYLE_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int KYLE_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Rodokuo
	public static final String RODOKUO_NAME = "Rodokuo";
	public static final String RODOKUO_DESCRIPTION = "Defeat Rodokuo.";
	public static final int RODOKUO_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int RODOKUO_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Kaboocha
	public static final String KABOOCHA_NAME = "Kaboocha";
	public static final String KABOOCHA_DESCRIPTION = "Defeat Kaboocha";
	public static final int KABOOCHA_IMAGE_LOCKED = R.drawable.achv_locked;
	public static final int KABOOCHA_IMAGE_EARNED = R.drawable.achv_unlocked;

	// Game Beat
	public static final String GAME_BEAT_NAME = "Game Beat";
	public static final String GAME_BEAT_DESCRIPTION = "Beat the game.";
	public static final int GAME_BEAT_IMAGE_LOCKED = R.drawable.ui_achievement_game_beat_locked;
	public static final int GAME_BEAT_IMAGE_EARNED = R.drawable.ui_achievement_game_beat;

	// Baby beat
	public static final String BABY_BEAT_NAME = "Baby Beat";
	public static final String BABY_BEAT_DESCRIPTION = "Beat the game on Baby difficulty.";
	public static final int BABY_BEAT_IMAGE_LOCKED = R.drawable.ui_achievement_baby_beat_locked;
	public static final int BABY_BEAT_IMAGE_EARNED = R.drawable.ui_achievement_baby_beat;

	// Kids beat
	public static final String KIDS_BEAT_NAME = "Kids Beat";
	public static final String KIDS_BEAT_DESCRIPTION = "Beat the game on Kids difficulty.";
	public static final int KIDS_BEAT_IMAGE_LOCKED = R.drawable.ui_achievement_kid_beat_locked;
	public static final int KIDS_BEAT_IMAGE_EARNED = R.drawable.ui_achievement_kid_beat;

	// Adults beat
	public static final String ADULT_BEAT_NAME = "Adult beat";
	public static final String ADULT_BEAT_DESCRIPTION = "Beat the game on Adults difficulty";
	public static final int ADULT_BEAT_IMAGE_LOCKED = R.drawable.ui_achievement_adult_beat_locked;
	public static final int ADULT_BEAT_IMAGE_EARNED = R.drawable.ui_achievement_adult_beat;

	// Untouchable
	public static final String UNTOUCHABLE_NAME = "Untouchable";
	public static final String UNTOUCHABLE_DESCRIPTION = "Beat a level without being hit.";
	public static final int UNTOUCHABLE_IMAGE_LOCKED = R.drawable.ui_achievement_untouchable_locked;
	public static final int UNTOUCHABLE_IMAGE_EARNED = R.drawable.ui_achievement_untouchable;

	// Merciful
	public static final String MERCIFUL_NAME = "Merciful";
	public static final String MERCIFUL_DESCRIPTION = "Beat a level without destroying any enemies.";
	public static final int MERCIFUL_IMAGE_LOCKED = R.drawable.ui_achievement_merciful_locked;
	public static final int MERCIFUL_IMAGE_EARNED = R.drawable.ui_achievement_merciful;

	// Diaries
	public static final int DIARIES_GOAL = 15;
	public static final String DIARIES_NAME = "Bookworm";
	public static final String DIARIES_DESCRIPTION = "Find all diaries.";
	public static final int DIARIES_IMAGE_LOCKED = R.drawable.ui_achievement_bookworm_locked;
	public static final int DIARIES_IMAGE_EARNED = R.drawable.ui_achievement_bookwork;

	// All Levels
	public static final int ALL_LEVELS_GOAL = 32;
	public static final String ALL_LEVELS_NAME = "All Levels";
	public static final String ALL_LEVELS_DESCRIPTION = "Beat each level in the game.";
	public static final int ALL_LEVELS_IMAGE_LOCKED = R.drawable.ui_achievement_all_levels_locked;
	public static final int ALL_LEVELS_IMAGE_EARNED = R.drawable.ui_achievement_all_levels;

	// Godlike
	public static final String GODLIKE_NAME = "Godlike";
	public static final String GODLIKE_DESCRIPTION = "Beat the game without dying.";
	public static final int GODLIKE_IMAGE_LOCKED = R.drawable.ui_achievement_godlike_locked;
	public static final int GODLIKE_IMAGE_EARNED = R.drawable.ui_achievement_godlike;

	// Gadgeteer
	public static final String GADGETEER_NAME = "Gadgeteer";
	public static final String GADGETEER_DESCRIPTION = "Buy all items available in the store.";
	public static final int GADGETEER_IMAGE_LOCKED = R.drawable.ui_achievement_gadgeteer_locked;
	public static final int GADGETEER_IMAGE_EARNED = R.drawable.ui_achievement_gadgeteer;

	// Window-Shopper
	public static final String WINDOW_SHOPPER_NAME = "Window-Shopper";
	public static final String WINDOW_SHOPPER_DECRIPTION = "Check out the store without buying anything.";
	public static final int WINDOW_SHOPPER_IMAGE_LOCKED = R.drawable.ui_achievement_window_shopper_locked;
	public static final int WINDOW_SHOPPER_IMAGE_EARNED = R.drawable.ui_achievement_window_shopper;

	// Stomper
	public static final int STOMPER_GOAL = 100;
	public static final String STOMPER_NAME = "Stomper";
	public static final String STOMPER_DESCRIPTION = "Perform " + STOMPER_GOAL + " stomps.";
	public static final int STOMPER_IMAGE_LOCKED = R.drawable.ui_achievement_stomper_locked;
	public static final int STOMPER_IMAGE_EARNED = R.drawable.ui_achievement_stomper;

}
