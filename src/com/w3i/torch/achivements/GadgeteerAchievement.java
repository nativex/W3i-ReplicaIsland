package com.w3i.torch.achivements;

import java.util.List;
import java.util.Map;

import com.w3i.torch.R;
import com.w3i.torch.gamesplatform.TorchItem;
import com.w3i.torch.gamesplatform.TorchItem.PurchaseState;
import com.w3i.torch.gamesplatform.TorchItemManager;

public class GadgeteerAchievement extends ProgressAchievement {

	public GadgeteerAchievement() {
		super(0);
		setName(R.string.achievement_gadgeteer_name);
		setDescription(R.string.achievement_gadgeteer_description);
		setType(Type.GADGETEER);
		setImageDone(R.drawable.ui_achievement_gadgeteer_locked);
		setImageLocked(R.drawable.ui_achievement_gadgeteer);
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
