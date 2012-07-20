package com.w3i.torch.achivements;

import android.content.SharedPreferences;

public abstract class Achievement {
	private Type type;
	private boolean disabled = false;
	private boolean done = false;
	private boolean progress = false;
	private String description = null;
	private String name = null;
	private int image = -1;
	private boolean locked = false;
	private String preferencesName;

	public static enum State {
		START,
		FINISH,
		FAIL;
	}

	public enum Type {
		CRYSTALS("Crystals"),
		PEARLS("Pearls"),
		GOOD_ENDING("GoodEnding"),
		FLY_TIME("FlyTime"),
		JETPACK_TIME("JetpackTime"),
		KILLS("Kills"),
		MEGA_KILL("MegaKill"),
		MULTI_KILL("MultiKill"),
		LEVELS("Levels"),
		HEALTH("Health"),
		BONUS_PEARLS("BonusPearls"),
		BONUS_CRYSTALS("BonusCrystals"),
		DEATH("Death"),
		HIT("Hit"),
		SHIELD("Shield"),
		POSSESSION("Possession"),
		KYLE_DEFEATED("KyleDefeated"),
		KABOCHA_DEFEATED("KabochaDefeated"),
		RODOKOU_DEFEATED("RodokouDefeated"),
		GAME_BEAT("GameBeat"),
		STOMP("Stomp"),
		BABY("BabyDiff"),
		KIDS("KidsDiff"),
		ADULT("AdultDiff"),
		UNTOUCHABLE("Untouchable"),
		MERCIFUL("Merciful"),
		DIARIES("Dieries"),
		All_LEVELS("AllLevels"),
		GODLIKE("Godlike"),
		GADGETEER("Gadgeteer"),
		WINDOW_SHOPPER("WindowShopper");

		private String preferencesString;

		private Type(String s) {
			preferencesString = "achv" + s;
		}

		public String getPreferencesString() {
			return preferencesString;
		}
	}

	protected Achievement() {
		preferencesName = getClass().getSimpleName();
	}

	public Type getType() {
		return type;
	}

	protected void setType(
			Type type) {
		this.type = type;
		preferencesName = type.getPreferencesString();
	}

	protected void setDescription(
			String description) {
		this.description = description;
	}

	protected void setName(
			String name) {
		this.name = name;
	}

	public void setDisabled(
			boolean disabled) {
		this.disabled = disabled;
	}

	protected void setProgressAchievement(
			boolean hasProgress) {
		progress = hasProgress;
	}

	protected void setImage(
			int resourceId) {
		image = resourceId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public int getImage() {
		return image;
	}

	public void setDone(
			boolean done) {
		setDone(done, true);
	}

	public void setDone(
			boolean done,
			boolean notify) {
		boolean fireListener = notify && done && !this.done;

		// Log.i("Achievement (" + getName() + ") is done: " + done);
		this.done = done;
		// AchievementManager.storeAchievements();
		if (fireListener) {
			AchievementManager.notifyAchievementDone(this);
		}
		if (locked) {
			setLocked(false);
		}
	}

	public boolean isDone() {
		return done;
	}

	public boolean isProgressAchievement() {
		return progress;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(
			boolean locked) {
		// Log.d("Achievement " + getName() + " locked status changed: " + locked);
		boolean notify = (!locked) && (this.locked);
		this.locked = locked;
		if (notify) {
			AchievementManager.notifyAchievementUnlocked(this);
		}
	}

	public void setPrefernecesName(
			String name) {
		preferencesName = name;
	}

	protected String getPreferencesName() {
		return preferencesName;
	}

	public String getPreferencesDisabled() {
		return preferencesName;
	}

	public String getPreferencesDone() {
		return preferencesName + "Done";
	}

	public String getPreferencesProgress() {
		return preferencesName + "Progress";
	}

	public String getPreferencesLocked() {
		return preferencesName + "Locked";
	}

	public void onState(
			State state) {
		// Override in achievements with states.
	}

	public void storeAdditionalSharedPreferencesData(
			SharedPreferences.Editor editor) {
		// Override in achievements with additional data.
	}

	public void loadAdditionalSharedPreferencesData(
			SharedPreferences preferences) {
		// Override in achievements with additional data.
	}

}
