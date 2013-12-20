package com.recharge.torch.inapppurchase;

import com.recharge.torch.inapppurchase.items.Crystals150;
import com.recharge.torch.inapppurchase.items.Crystals50;
import com.recharge.torch.inapppurchase.items.DiscoveryWall;
import com.recharge.torch.inapppurchase.items.PackMedium;
import com.recharge.torch.inapppurchase.items.PackSmall;
import com.recharge.torch.inapppurchase.items.Pearls1000Item;
import com.recharge.torch.inapppurchase.items.Pearls3000Item;

public enum MarketItems {
	CRYSTALS_150(Crystals150.class, "android.test.purchased"),
	CRYSTALS_50(Crystals50.class, "android.test.purchased"),
	DISCOVERY_WALL(DiscoveryWall.class, "android.test.purchased"),
	PEARLS_1000(Pearls1000Item.class, "android.test.canceled"),
	PEARLS_3000(Pearls3000Item.class, "android.test.canceled"),
	PACK_SMALL(PackSmall.class, "android.test.refunded"),
	PACK_MEDIUM(PackMedium.class, "android.test.refunded"),
	UNAVAILABLE(null, "android.test.item_unavailable");

	private Class<? extends MarketItem> itemClass;
	private String marketId;

	private MarketItems(Class<? extends MarketItem> clz, String id) {
		itemClass = clz;
		marketId = id;
	}

	public String getMarketId() {
		return marketId;
	}

	public MarketItem getItemInstance() {
		try {
			return itemClass.newInstance();
		} catch (Exception e) {
		}
		return null;
	}

	public static MarketItems findItemByMarketId(
			String marketId) {
		for (MarketItems item : values()) {
			if (item.marketId.equals(marketId)) {
				return item;
			}
		}
		return UNAVAILABLE;
	}
}
