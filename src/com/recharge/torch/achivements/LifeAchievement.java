package com.recharge.torch.achivements;

import java.util.List;

import com.recharge.torch.R;
import com.recharge.torch.gamesplatform.TorchItem;
import com.recharge.torch.gamesplatform.TorchItemManager;
import com.recharge.torch.powerups.PowerupTypes;

public class LifeAchievement extends Achievement {

	public LifeAchievement() {
		setName(R.string.achievement_health_name);
		setDescription(R.string.achievement_health_description);
		setType(Type.HEALTH);
		setImageDone(R.drawable.ui_achievement_max_life);
		setImageLocked(R.drawable.ui_achievement_max_life_locked);
	}

	@Override
	public void onState(
			State state) {
		if (isDone()) {
			return;
		}
		List<TorchItem> lifeUpgrades = TorchItemManager.getItemsWithAttribute(PowerupTypes.LIFE_POINTS);
		if ((lifeUpgrades != null) && (lifeUpgrades.size() > 0)) {
			for (TorchItem item : lifeUpgrades) {
				if (!item.isPurchased()) {
					setDone(false);
					return;
				}
			}
			setDone(true);
		}
	}
}