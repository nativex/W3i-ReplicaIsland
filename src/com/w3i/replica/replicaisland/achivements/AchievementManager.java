package com.w3i.replica.replicaisland.achivements;

import java.util.ArrayList;
import java.util.List;

import com.w3i.common.Log;

public class AchievementManager {
	private static AchievementManager instance = null;
	private ArrayList<Achievement> achivements = null;
	private static int instances = 0;

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
		if (achv != null) {
			achv.setDone(isDone);
		} else {
			Log.e("AchivementManager._setAchivementDone: cannot find achivement of type: " + type.toString());
		}
	}

	private void _release() {
		instance.achivements.clear();
		instance.achivements = null;
		instance = null;
		instances--;
	}

}
