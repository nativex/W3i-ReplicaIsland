package com.w3i.torch.achivements;

import java.util.List;

import com.w3i.torch.R;
import com.w3i.torch.gamesplatform.TorchItem;
import com.w3i.torch.gamesplatform.TorchItemManager;
import com.w3i.torch.powerups.PowerupTypes;

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
		boolean done = true;
		List<TorchItem> lifeUpgrades = TorchItemManager.getItemsWithAttribute(PowerupTypes.LIFE_POINTS);
		if (lifeUpgrades != null) {
			for (TorchItem item : lifeUpgrades) {
				if (!item.isPurchased()) {
					done = false;
				}
			}
			setDone(done);
		}
	}
}
