package com.recharge.torch.achivements;

import java.util.List;
import java.util.Map;

import com.recharge.torch.R;
import com.recharge.torch.gamesplatform.TorchItem;
import com.recharge.torch.gamesplatform.TorchItem.PurchaseState;
import com.recharge.torch.gamesplatform.TorchItemManager;

public class GadgeteerAchievement extends ProgressAchievement {

	public GadgeteerAchievement() {
		super(0);
		setName(R.string.achievement_gadgeteer_name);
		setDescription(R.string.achievement_gadgeteer_description);
		setType(Type.GADGETEER);
		setImageDone(R.drawable.ui_achievement_gadgeteer);
		setImageLocked(R.drawable.ui_achievement_gadgeteer_locked);
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
