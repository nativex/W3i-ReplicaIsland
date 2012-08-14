package com.w3i.torch.achivements;

import java.util.List;

import com.w3i.torch.gamesplatform.TorchItem;
import com.w3i.torch.gamesplatform.TorchItemManager;
import com.w3i.torch.powerups.PowerupTypes;

public class LifeAchievement extends Achievement {

	public LifeAchievement() {
		setName(AchievementConstants.HEALTH_NAME);
		setDescription(AchievementConstants.HEALTH_DESCRIPTION);
		setType(Type.HEALTH);
		setImageDone(AchievementConstants.HEALTH_IMAGE_EARNED);
		setImageLocked(AchievementConstants.HEALTH_IMAGE_LOCKED);
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
