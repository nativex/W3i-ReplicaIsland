package com.w3i.torch.achivements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.w3i.common.Log;
import com.w3i.torch.achivements.Achievement.Type;

public class AchievementManager {
	private Context applicationContext = null;
	private static AchievementManager instance = null;
	private ArrayList<Achievement> achievements = null;
	private static int instances = 0;
	private double flyTime = 0d;
	private double jetpackTime = 0d;
	private boolean flying = false;
	private boolean boosting = false;

	private FlyTime flyTimeAchievement;
	private JetpackTime jetpackTimeAchievement;
	private AchievementListener achievementListener;

	private AchievementManager() {
		achievements = new ArrayList<Achievement>();
		instances++;
		if (instances > 1) {
			Log.e("AchivementManager: too many instances created");
		}
		_initializeAchievements();
	}

	private static synchronized void checkInstance() {
		if (instance == null) {
			instance = new AchievementManager();
		}
	}

	public static void addAchivement(
			Achievement achivement) {
		checkInstance();
		instance._addAchivement(achivement);
	}

	private void _addAchivement(
			Achievement achivement) {
		if (achivement != null) {
			achievements.add(achivement);
		}
	}

	public static List<Achievement> getAchivements() {
		checkInstance();
		return instance._getAchivements();
	}

	private List<Achievement> _getAchivements() {
		return achievements;
	}

	public static Achievement getAchivement(
			Achievement.Type type) {
		checkInstance();
		return instance._getAchivement(type);
	}

	private Achievement _getAchivement(
			Achievement.Type type) {
		for (Achievement a : achievements) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}

	public static void setAchievementProgress(
			Achievement.Type type,
			int progress) {
		Achievement achievement = getAchivement(type);
		instance._setAchievementProgress(achievement, progress);
	}

	public static void setAchievementProgress(
			Achievement achievement,
			int progress) {
		checkInstance();
		instance._setAchievementProgress(achievement, progress);
	}

	private void _setAchievementProgress(
			Achievement achievement,
			int progress) {
		if ((achievement != null) && (achievement instanceof ProgressAchievement)) {
			ProgressAchievement progressAchievement = (ProgressAchievement) achievement;
			progressAchievement.setProgress(progress);
		}
	}

	public static void setAchievementGoal(
			Achievement.Type type,
			int goal) {
		checkInstance();
		instance._setAchievementGoal(type, goal);
	}

	private void _setAchievementGoal(
			Achievement.Type type,
			int goal) {
		Achievement achievement = _getAchivement(type);
		if ((achievement != null) && (achievement instanceof ProgressAchievement)) {
			((ProgressAchievement) achievement).setGoal(goal);
		}
	}

	public static void incrementAchievementProgress(
			Achievement.Type type,
			int increment) {
		Achievement achievement = getAchivement(type);
		instance._incrementAchievementProgress(achievement, increment);
	}

	public static void incrementAchievementProgress(
			Achievement achievement,
			int increment) {
		checkInstance();
		instance._incrementAchievementProgress(achievement, increment);
	}

	private void _incrementAchievementProgress(
			Achievement achievement,
			int increment) {
		if ((achievement != null) && (achievement instanceof ProgressAchievement)) {
			ProgressAchievement progressAchievement = (ProgressAchievement) achievement;
			progressAchievement.increaseProgress(increment);
		}
	}

	public synchronized void release() {
		if (instance == null) {
			return;
		}
		instance._release();
	}

	public static void setAchievementDone(
			Achievement.Type type,
			boolean isDone) {
		checkInstance();
		instance._setAchievementDone(type, isDone);
	}

	private void _setAchievementDone(
			Achievement.Type type,
			boolean isDone) {
		Achievement achv = _getAchivement(type);
		if ((achv != null) && (achv.isDone() != isDone)) {
			achv.setDone(isDone);
		} else {
			Log.e("AchivementManager._setAchievementDone: cannot find achivement of type: " + type.toString());
		}
	}

	public static boolean isAchievementDone(
			Achievement.Type type) {
		if (instance == null) {
			return false;
		}
		return instance._isAchievementDone(type);
	}

	private boolean _isAchievementDone(
			Achievement.Type type) {
		Achievement achievement = _getAchivement(type);
		if (achievement != null) {
			return achievement.isDone();
		}
		return false;
	}

	public static void updateFlyTime(
			double delta,
			boolean touchingGround) {
		checkInstance();
		instance._updateFlyTime(delta, touchingGround);
	}

	private synchronized void _updateFlyTime(
			double delta,
			boolean touchingGround) {
		if (!touchingGround) {
			flyTime += delta;
			flying = true;
			// Log.d("Flytime till now " + flyTime);
		} else if (flying) {
			if (flyTimeAchievement == null) {
				Achievement achv = AchievementManager.getAchivement(Type.FLY_TIME);
				if ((achv != null) && (achv instanceof FlyTime)) {
					flyTimeAchievement = (FlyTime) achv;
				} else {
					return;
				}
			}
			int timeFlown = (int) (flyTime + 0.5);
			// Log.d("Flytime flown = " + timeFlown);
			flyTimeAchievement.increaseProgress(timeFlown);
			flyTime = 0d;
			flying = false;
		}
	}

	public static void updateJetpackTime(
			double delta,
			boolean usingJetpack) {
		checkInstance();
		instance._updateJetpackTime(delta, usingJetpack);
	}

	private synchronized void _updateJetpackTime(
			double delta,
			boolean usingJetpack) {
		if (usingJetpack) {
			jetpackTime += delta;
			boosting = true;
		} else if (boosting) {
			if (jetpackTimeAchievement == null) {
				Achievement achv = AchievementManager.getAchivement(Type.JETPACK_TIME);
				if ((achv != null) && (achv instanceof JetpackTime)) {
					jetpackTimeAchievement = (JetpackTime) achv;
				} else {
					return;
				}
			}
			int timeFlown = (int) (jetpackTime + 0.5);
			// Log.d("JetpackTime flown = " + timeFlown);
			jetpackTimeAchievement.increaseProgress(timeFlown);
			jetpackTime = 0d;
			boosting = false;
		}
	}

	public static void storeAchievements(
			SharedPreferences preferences) {
		if (instance != null) {
			for (Achievement a : instance.achievements) {
				a.storeSharedPreferencesData(preferences.edit());
			}
		}
	}

	public static void loadAchievements(
			SharedPreferences preferences) {
		checkInstance();
		instance._initializeAchievements();
		for (Achievement a : instance.achievements) {
			a.loadSharedPreferencesData(preferences);
		}
	}

	public static void loadAchievement(
			Achievement.Type type,
			SharedPreferences preferences) {
		checkInstance();
		instance._loadAchievement(instance._getAchivement(type), preferences);
	}

	public static void loadAchievement(
			Achievement achievement,
			SharedPreferences preferences) {
		checkInstance();
		instance._loadAchievement(achievement, preferences);
	}

	private void _loadAchievement(
			Achievement achievement,
			SharedPreferences preferences) {
		if (achievement != null) {
			achievement.loadSharedPreferencesData(preferences);
		}
	}

	public static void storeAchievement(
			Achievement.Type type,
			SharedPreferences preferences) {
		checkInstance();
		instance._storeAchievement(instance._getAchivement(type), preferences);
	}

	public static void storeAchievement(
			Achievement achievement,
			SharedPreferences preferences) {
		checkInstance();
		instance._storeAchievement(achievement, preferences);
	}

	private void _storeAchievement(
			Achievement achievement,
			SharedPreferences preferences) {
		if (achievement != null) {
			achievement.storeSharedPreferencesData(preferences.edit());
		}
	}

	public static void notifyAchievementDone(
			Achievement achievement) {
		checkInstance();
		instance._notifyAchievementDone(achievement);
	}

	private void _notifyAchievementDone(
			Achievement achievement) {
		if (achievementListener != null) {
			achievementListener.achievementDone(achievement);
		}
	}

	public static void notifyAchievementProgressUpdated(
			Achievement achievement,
			int percentDone) {
		checkInstance();
		instance._notifyAchievementProgressUpdated(achievement, percentDone);
	}

	private void _notifyAchievementProgressUpdated(
			Achievement achievement,
			int percentDone) {
		if (achievementListener != null) {
			achievementListener.achievementProgressUpdate(achievement, percentDone);
		}
	}

	public static void registerAchievementListener(
			AchievementListener listener) {
		checkInstance();
		instance._registerAchievementListener(listener);
	}

	private void _registerAchievementListener(
			AchievementListener listener) {
		achievementListener = listener;
	}

	public static boolean hasAchievementListenerRegistered() {
		if (instance == null) {
			return false;
		}
		return instance._hasAchievementListenerRegistered();
	}

	private boolean _hasAchievementListenerRegistered() {
		return achievementListener != null;
	}

	/**
	 * TEST METHOD
	 */
	public static void resetAchievements() {
		if (instance == null) {
			return;
		}
		instance._resetAchievements();
	}

	/**
	 * TEST METHOD
	 */
	public static void unlockAchievements() {
		if (instance == null) {
			return;
		}
		instance._unlockAchievements();
	}

	private void _unlockAchievements() {
		for (Achievement a : achievements) {
			a.setLocked(false, false);
		}
	}

	private void _resetAchievements() {
		if (achievements != null) {
			for (Achievement achv : achievements) {
				achv.reset();
			}
		}
	}

	public static void setAchievementLocked(
			Achievement.Type type,
			boolean isLocked) {
		checkInstance();
		instance._setAchievementLocked(type, isLocked);
	}

	private void _setAchievementLocked(
			Achievement.Type type,
			boolean isLocked) {
		Achievement achievement = _getAchivement(type);
		if (achievement != null) {
			achievement.setLocked(isLocked);
		}
	}

	public static void setAchievementState(
			Achievement.Type type,
			Achievement.State state) {
		if (instance == null) {
			return;
		}
		instance._setAchievementState(type, state, null);
	}

	public static <T> void setAchievementState(
			Achievement.Type type,
			Achievement.State state,
			AchievementData<T> data) {
		if (instance == null) {
			return;
		}
		instance._setAchievementState(type, state, data);
	}

	private <T> void _setAchievementState(
			Achievement.Type type,
			Achievement.State state,
			AchievementData<T> data) {
		Achievement achievement = _getAchivement(type);
		if (achievement != null) {
			if (data == null) {
				achievement.onState(state);
			} else {
				achievement.onState(state, data);
			}
		}
	}

	private void _initializeAchievements() {
		achievements.clear();
		achievements.add(new BonusCrystalsAchievement());
		achievements.add(new BonusPearlsAchievement());
		achievements.add(new CrystalsAchievement());
		achievements.add(new DeathAchievement());
		achievements.add(new FlyTime());
		achievements.add(new GameBeatAchievement());
		achievements.add(new GoodEndingAchievement());
		achievements.add(new HitAchievement());
		achievements.add(new JetpackTime());
		achievements.add(new KillsAchievement());
		achievements.add(new LevelsAchievement());
		achievements.add(new LifeAchievement());
		achievements.add(new MegakillAchievement());
		achievements.add(new MultikillAchievement());
		achievements.add(new PearlsAchievement());
		achievements.add(new PossessionAchievement());
		achievements.add(new ShieldAchievement());
		achievements.add(new KyleDefeatedAchievement());
		achievements.add(new KabochaDefeated());
		achievements.add(new RodokouDefeated());
		achievements.add(new BabyDifficultyAchievement());
		achievements.add(new KidsDifficultyAchievement());
		achievements.add(new AdultsDifficultyAchievement());
		achievements.add(new UntouchableAchievement());
		achievements.add(new MercifulAchievement());
		achievements.add(new DiariesAchievement());
		achievements.add(new AllLevelsAchievement());
		achievements.add(new GodlikeAchievement());
		achievements.add(new GadgeteerAchievement());
		achievements.add(new WindowShopperAchievement());
		achievements.add(new StomperAchievement());
		Collections.sort(achievements, new Comparator<Achievement>() {

			@Override
			public int compare(
					Achievement object1,
					Achievement object2) {
				if (object1.getType().ordinal() > object2.getType().ordinal()) {
					return 1;
				} else if (object1.getType().ordinal() < object2.getType().ordinal()) {
					return -1;
				}
				return 0;
			}
		});
	}

	private void _release() {
		instance.achievements.clear();
		instance.achievements = null;
		instance = null;
		instances--;
	}

	public static void notifyAchievementUnlocked(
			Achievement achievement) {
		if (instance == null) {
			return;
		}
		instance._notifyAchievementUnlocked(achievement);
	}

	private void _notifyAchievementUnlocked(
			Achievement achievement) {
		if (achievementListener != null) {
			achievementListener.achievementUnlocked(achievement);
		}
	}

	public static void setApplicationContext(
			Context context) {
		checkInstance();
		instance.applicationContext = context;
	}

	public static Context getApplicationContext() {
		if (instance == null) {
			return null;
		}
		return instance.applicationContext;
	}
}
