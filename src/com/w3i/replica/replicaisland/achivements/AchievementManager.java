package com.w3i.replica.replicaisland.achivements;

import java.util.ArrayList;
import java.util.List;

import com.w3i.common.Log;
import com.w3i.replica.replicaisland.achivements.Achievement.Type;
import com.w3i.replica.replicaisland.store.SharedPreferenceManager;

public class AchievementManager {
	private static AchievementManager instance = null;
	private ArrayList<Achievement> achivements = null;
	private static int instances = 0;

	private FlyTime flyTime;
	private JetpackTime jetpackTime;
	private AchievementListener achievementListener;

	private AchievementManager() {
		achivements = new ArrayList<Achievement>();
		instances++;
		if (instances > 1) {
			Log.e("AchivementManager: too many instances created");
		}
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
			achivements.add(achivement);
		}
	}

	public static List<Achievement> getAchivements() {
		checkInstance();
		return instance._getAchivements();
	}

	private List<Achievement> _getAchivements() {
		return achivements;
	}

	public static Achievement getAchivement(
			Achievement.Type type) {
		checkInstance();
		return instance._getAchivement(type);
	}

	private Achievement _getAchivement(
			Achievement.Type type) {
		for (Achievement a : achivements) {
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

	public static void setAchivementDone(
			Achievement.Type type,
			boolean isDone) {
		checkInstance();
		instance._setAchivementDone(type, isDone);
	}

	private void _setAchivementDone(
			Achievement.Type type,
			boolean isDone) {
		Achievement achv = _getAchivement(type);
		if ((achv != null) && (achv.isDone() != isDone)) {
			achv.setDone(isDone);
		} else {
			Log.e("AchivementManager._setAchivementDone: cannot find achivement of type: " + type.toString());
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

	public static void increaseFlyTime(
			double delta) {
		checkInstance();
		instance._increseFlyTime(delta);
	}

	private void _increseFlyTime(
			double delta) {
		if (flyTime == null) {
			Achievement achv = AchievementManager.getAchivement(Type.FLY_TIME);
			if ((achv != null) && (achv instanceof FlyTime)) {
				flyTime = (FlyTime) achv;
			} else {
				return;
			}
		}
		flyTime.updateFlying(delta);
	}

	public static void endFlyTime() {
		checkInstance();
		instance._endFlyTime();
	}

	private void _endFlyTime() {
		if (flyTime == null) {
			Achievement achv = AchievementManager.getAchivement(Type.FLY_TIME);
			if ((achv != null) && (achv instanceof FlyTime)) {
				flyTime = (FlyTime) achv;
			} else {
				return;
			}
		}
		flyTime.endFlying();

	}

	public static void startJetpackTime(
			double delta) {
		checkInstance();
		instance._startJetpackTime(delta);
	}

	private void _startJetpackTime(
			double delta) {
		if (jetpackTime == null) {
			Achievement achv = AchievementManager.getAchivement(Type.JETPACK_TIME);
			if ((achv != null) && (achv instanceof JetpackTime)) {
				jetpackTime = (JetpackTime) achv;
			} else {
				return;
			}
		}
		if (jetpackTime.isFlying()) {
			jetpackTime.updateFlying(delta);
		} else {
			jetpackTime.startedFlaying();
		}
	}

	public static void endJetpackTime() {
		checkInstance();
		instance._endJetpackTime();
	}

	private void _endJetpackTime() {
		if (jetpackTime == null) {
			Achievement achv = AchievementManager.getAchivement(Type.JETPACK_TIME);
			if ((achv != null) && (achv instanceof JetpackTime)) {
				jetpackTime = (JetpackTime) achv;
			} else {
				return;
			}
		}
		if (!jetpackTime.isFlying()) {
			return;
		}
		jetpackTime.endFlying();

	}

	public static void storeAchievements() {
		if (instance != null) {
			SharedPreferenceManager.storeAchivements();
		}
	}

	public static void loadAchievements() {
		checkInstance();
		SharedPreferenceManager.loadAchivements();
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

	public static void registerAchievementListener(
			AchievementListener listener) {
		checkInstance();
		instance._registerAchievementListener(listener);
	}

	private void _registerAchievementListener(
			AchievementListener listener) {
		achievementListener = listener;
	}

	/**
	 * TEST METHOD
	 */
	public static void resetAchievements() {
		if (instance == null) {
			return;
		}

		for (Achievement a : instance.achivements) {
			a.setDone(false, false);
			if (a instanceof ProgressAchievement) {
				((ProgressAchievement) a).setProgress(0);
			}
		}
	}

	private void _release() {
		instance.achivements.clear();
		instance.achivements = null;
		instance = null;
		instances--;
	}

}
