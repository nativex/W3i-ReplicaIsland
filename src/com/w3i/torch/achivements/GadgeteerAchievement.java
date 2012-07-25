package com.w3i.torch.achivements;

public class GadgeteerAchievement extends ProgressAchievement {
	private static int itemsCount = 0;
	private static int purchasedItems = 0;

	public static void setItemsCount(
			int itemsCount) {
		GadgeteerAchievement.itemsCount = itemsCount;
		AchievementManager.setAchievementState(Type.GADGETEER, State.SET_GOAL, new AchievementData<Integer>(itemsCount));
	}

	public static void setPurchasedItems(
			int itemsCount) {
		purchasedItems = itemsCount;
		AchievementManager.setAchievementState(Type.GADGETEER, State.SET_PROGRESS, new AchievementData<Integer>(itemsCount));
	}

	public GadgeteerAchievement() {
		super(itemsCount);
		setName(AchievementConstants.GADGETEER_NAME);
		setDescription(AchievementConstants.GADGETEER_DESCRIPTION);
		setType(Type.GADGETEER);
		if (itemsCount == 0) {
			setInitialized(false);
		}
		if (purchasedItems > 0) {
			setProgress(itemsCount);
		}
	}

	@Override
	public void setGoal(
			int goal) {
		super.setGoal(goal);
		if (goal > 0) {
			setInitialized(true);
		}
	}

	@Override
	public void onState(
			State state) {
		switch (state) {
		case INITIALIZE:
			setInitialized(true);
			break;
		}
	}

	@Override
	public <T> void onState(
			State state,
			AchievementData<T> data) {

		switch (state) {
		case SET_PROGRESS:
			if (data.getData() instanceof Integer) {
				setProgress((Integer) data.getData());
			}
			break;

		case SET_GOAL:
			if (data.getData() instanceof Integer) {
				setGoal((Integer) data.getData());
			}
		}
	}
}
