package com.recharge.torch.store.upgrades;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.recharge.torch.general.Attribute;
import com.recharge.torch.general.Upgrade;
import com.recharge.torch.store.attributes.Attributes;
import com.recharge.torch.utils.PreferencesManager;

public enum Upgrades {
	ALUMINIUM_PLATING(AluminiumPlatingUpgrade.class),
	TITANIUM_PLATING(TitaniumPlatingUpgrade.class),
	CARBON_PLATING(CarbonPlatingUpgrade.class),
	NANOBOTS_PLATING(NanobotsPlatingUpgrade.class),
	TWIN_BATTERIES(TwinBatteriesUpgrade.class),
	NUCLEAR_BATTERIES(NuclearBatteriesUpgrade.class),
	GARBAGE_COLLECTOR(GarbageCollectorUpgrade.class),
	KILLING_SPREE(KillingSpreeUpgrade.class),
	CRYSTAL_EXTRACTOR(CrystalExtractorUpgrade.class),
	NUCLEAR_POWER_CELL(NuclearPowerCellUpgrade.class),
	ENERGY_STABILIZER(EnergyStabilizerUpgrade.class);

	private Class<? extends Upgrade> upgradeClass;
	private Upgrade upgrade = null;

	private Upgrades(Class<? extends Upgrade> upgrade) {
		upgradeClass = upgrade;
	}

	@Expose
	private boolean owned = false;

	public Upgrade getUpgrade() {
		if (upgrade == null) {
			try {
				upgrade = upgradeClass.newInstance();
			} catch (Exception e) {
				// This should never happen.
			}
		}
		return upgrade;
	}

	public static final void reset() {
		for (Upgrades upgrade : values()) {
			upgrade.upgrade = null;
		}
	}

	public void setOwned(
			boolean isOwned) {
		owned = isOwned;
	}

	public boolean isOwned() {
		return owned;
	}

	public void buy() {
		if (!owned) {
			Upgrade item = getUpgrade();
			if (item.isAffordable()) {
				setOwned(true);
				item.buy();
				item.consume();
				store();
			}
		}
	}

	public static final boolean store() {
		if (PreferencesManager.isInitialized()) {
			try {
				List<DataHolder> data = new ArrayList<DataHolder>();
				for (Upgrades upgrade : values()) {
					DataHolder holder = new DataHolder();
					holder.itemType = upgrade.name();
					holder.owned = upgrade.owned;
					data.add(holder);
				}
				String json = new Gson().toJson(data);
				PreferencesManager.storeOwnedItems(json);
				return true;
			} catch (Exception e) {
				Log.e("Torch", "Failed to store owned items", e);
			}
		}
		return false;
	}

	public static final boolean load() {
		if (PreferencesManager.isInitialized()) {
			try {
				String json = PreferencesManager.loadOwnedItems();
				if (json != null) {
					List<DataHolder> data = new Gson().fromJson(json, (new TypeToken<List<DataHolder>>() {
					}).getType());
					if ((data != null) && (data.size() > 0)) {
						Attributes.reset();
						for (DataHolder holder : data) {
							Upgrades upgrade = Upgrades.valueOf(holder.itemType);
							upgrade.owned = holder.owned;
							if (holder.owned) {
								List<Attribute> attributes = upgrade.getUpgrade().getAttributes();
								for (Attribute a : attributes) {
									a.consume();
								}
							}
						}
					}
					return true;
				}
			} catch (Exception e) {
				Log.e("Torch", "Failed to load owned items", e);
			}
		}
		return false;
	}

	private static class DataHolder {
		private String itemType;
		private boolean owned;
	}
}
