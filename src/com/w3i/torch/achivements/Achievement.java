package com.w3i.torch.achivements;

import android.content.SharedPreferences;

import com.w3i.torch.R;

public abstract class Achievement {
	private Type type;
	private boolean disabled = false;
	private boolean done = false;
	private boolean progress = false;
	private String description = null;
	private String name = null;
	private int imageNotDone = R.drawable.achv_locked;
	private int imageDone = R.drawable.achv_unlocked;
	private boolean locked = false;
	private String preferencesName;
	private boolean preferencesLoaded = false;
	private boolean initialized = true;

	/**
	 * @return the preferencesLoaded
	 */
	public boolean isPreferencesLoaded() {
		return preferencesLoaded;
	}

	/**
	 * @param preferencesLoaded
	 *            the preferencesLoaded to set
	 */
	public void setPreferencesLoaded(
			boolean preferencesLoaded) {
		this.preferencesLoaded = preferencesLoaded;
	}

	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param initialized
	 *            the initialized to set
	 */
	public void setInitialized(
			boolean initialized) {
		this.initialized = initialized;
	}

	public static enum State {
		START,
		FINISH,
		FAIL,
		UPDATE,
		INITIALIZE,
		SET_GOAL,
		SET_PROGRESS;
	}

	public enum Type {
		PEARLS("Pearls"),
		CRYSTALS("Crystals"),
		BONUS_PEARLS("BonusPearls"),
		BONUS_CRYSTALS("BonusCrystals"),
		FLY_TIME("FlyTime"),
		JETPACK_TIME("JetpackTime"),
		HEALTH("Health"),
		KILLS("Kills"),
		MEGA_KILL("MegaKill"),
		MULTI_KILL("MultiKill"),
		SHIELD("Shield"),
		POSSESSION("Possession"),
		STOMP("Stomp"),
		HIT("Hit"),
		DEATH("Death"),
		LEVELS("Levels"),
		DIARIES("Dieries"),
		All_LEVELS("AllLevels"),
		GAME_BEAT("GameBeat"),
		GOOD_ENDING("GoodEnding"),
		BABY("BabyDiff"),
		KIDS("KidsDiff"),
		ADULT("AdultDiff"),
		KYLE_DEFEATED("KyleDefeated"),
		KABOCHA_DEFEATED("KabochaDefeated"),
		RODOKOU_DEFEATED("RodokouDefeated"),
		UNTOUCHABLE("Untouchable"),
		MERCIFUL("Merciful"),
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

	protected void setImageLocked(
			int resourceId) {
		imageNotDone = resourceId;
	}

	protected void setImageDone(
			int imageResource) {
		imageDone = imageResource;
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
		if (isDone()) {
			return imageDone;
		} else {
			return imageNotDone;
		}
	}

	public void setDone(
			boolean done) {
		setDone(done, true);
	}

	public void setDone(
			boolean done,
			boolean notify) {
		if (!initialized) {
			return;
		}
		boolean fireListener = notify && done && !this.done;
		// Log.i("Achievement (" + getName() + ") is done: " + done);
		this.done = done;
		if (done) {
			setLocked(false);
		}
		if (fireListener) {
			AchievementManager.notifyAchievementDone(this);
		}
		if (preferencesLoaded) {
			AchievementManager.storeAchievement(this);
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

	public void setLocked(
			boolean locked) {
		setLocked(locked, true);
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(
			boolean locked,
			boolean notify) {
		// Log.d("Achievement " + getName() + " locked status changed: " + locked);
		boolean showToast = notify && (!locked) && (this.locked);
		this.locked = locked;
		if (showToast) {
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

	public <T> void onState(
			State state,
			AchievementData<T> data) {
		// Override in achievements with states.
	}

	public void loadSharedPreferencesData(
			SharedPreferences preferences) {
		setDisabled(preferences.getBoolean(getPreferencesDisabled(), false));
		setDone(preferences.getBoolean(getPreferencesDone(), false));
		if (preferences.contains(getPreferencesLocked())) {
			setLocked(preferences.getBoolean(getPreferencesLocked(), false), false);
		}
		preferencesLoaded = true;
	}

	public void storeSharedPreferencesData(
			SharedPreferences.Editor editor) {
		editor.putBoolean(getPreferencesDisabled(), isDisabled());
		editor.putBoolean(getPreferencesDone(), isDone());
		editor.putBoolean(getPreferencesLocked(), isLocked());
		editor.commit();
	}

	public void reset() {
		setDone(false);
	}
}
