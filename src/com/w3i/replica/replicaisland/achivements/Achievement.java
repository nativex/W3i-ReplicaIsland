package com.w3i.replica.replicaisland.achivements;

import com.w3i.replica.replicaisland.store.SharedPreferenceManager;

public abstract class Achievement {
	private Type type;
	private boolean disabled = false;
	private boolean done = false;
	private boolean progress = false;
	private String description = null;
	private String name = null;
	private int image = -1;

	public enum Type {
		CRYSTALS,
		PEARLS,
		GOOD_ENDING,
		FLY_TIME,
		JETPACK_TIME
	}

	public Type getType() {
		return type;
	}

	protected void setType(
			Type type) {
		this.type = type;
	}

	protected void setDescription(
			String description) {
		this.description = description;
	}

	protected void setName(
			String name) {
		this.name = name;
	}

	protected void setDisabled(
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
		this.done = done;
		AchievementManager.storeAchievements();
	}

	public boolean isDone() {
		return done;
	}

	public boolean isProgressAchievement() {
		return progress;
	}

}
