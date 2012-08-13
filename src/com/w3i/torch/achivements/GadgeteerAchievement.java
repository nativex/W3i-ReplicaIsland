package com.w3i.torch.achivements;

import java.util.List;
import java.util.Map;

import com.w3i.torch.gamesplatform.TorchItem;
import com.w3i.torch.gamesplatform.TorchItemManager;
import com.w3i.torch.gamesplatform.TorchItem.PurchaseState;

public class GadgeteerAchievement extends ProgressAchievement {

	public GadgeteerAchievement() {
		super(0);
		setName(AchievementConstants.GADGETEER_NAME);
		setDescription(AchievementConstants.GADGETEER_DESCRIPTION);
		setType(Type.GADGETEER);
		setImageDone(AchievementConstants.GADGETEER_IMAGE_EARNED);
		setImageLocked(AchievementConstants.GADGETEER_IMAGE_LOCKED);
		setInitialized(false);
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
		Map<TorchItem.PurchaseState, List<TorchItem>> allItems = TorchItemManager.getAllItems();
		List<TorchItem> purchased = allItems.get(PurchaseState.PURCHASED);
		List<TorchItem> available = allItems.get(PurchaseState.AVAILABLE);

		switch (state) {
		case SET_PROGRESS:
			setProgress(purchased.size());
		case SET_GOAL:
			setGoal(purchased.size() + available.size());
		}
	}
}
