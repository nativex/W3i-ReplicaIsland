package com.w3i.torch.achivements;

import java.util.List;
import java.util.Map;

import com.w3i.torch.gamesplatform.TorchItem;
import com.w3i.torch.gamesplatform.TorchItem.PurchaseState;
import com.w3i.torch.gamesplatform.TorchItemManager;

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
		int purchased = allItems.get(PurchaseState.PURCHASED).size();
		int available = allItems.get(PurchaseState.AVAILABLE).size();
		setProgress(purchased);
		setGoal(purchased + available);
	}
}
