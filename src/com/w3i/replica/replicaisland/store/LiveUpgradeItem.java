package com.w3i.replica.replicaisland.store;

import com.w3i.replica.replicaisland.R;

public class LiveUpgradeItem extends Item<Integer> {

	public enum LIFE_UPGRADE_LEVEL {
		SMALL("Small", 1, 100), MEDIUM("Medium", 2, 300), LARGE("Large", 3,
				1000), ULTIMATE("Ultimare", 5, 3000);

		private String name;
		private int lives;
		private long price;
		private int icon;

		public String getName() {
			return name;
		}

		public int getLives() {
			return lives;
		}

		public long getPrice() {
			return price;
		}

		public int getIcon() {
			return icon;
		}

		private LIFE_UPGRADE_LEVEL(String name, int lives, long price) {
			this.name = name;
			this.lives = lives;
			this.price = price;
			this.icon = R.drawable.live_upgrade_icon;
		}
	}

	public LiveUpgradeItem(LIFE_UPGRADE_LEVEL upgradeLevel) {
		setPowerupValue(upgradeLevel.getLives());
		setName(upgradeLevel.getName() + " Live Upgrade");
		setDescription("Adds " + upgradeLevel.getLives() + " points of live.");
		setPrice(upgradeLevel.getPrice());
		setIcon(upgradeLevel.getIcon());
	}
}
