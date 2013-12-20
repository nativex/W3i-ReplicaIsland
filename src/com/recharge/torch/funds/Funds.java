package com.recharge.torch.funds;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.recharge.torch.general.Currency;
import com.recharge.torch.general.OnCurrencyChanged;
import com.recharge.torch.utils.PreferencesManager;

public enum Funds {
	PEARLS("pearls") {
		@Override
		public Currency createPrice(
				long amount) {
			Pearl pearls = new Pearl();
			pearls.set(amount);
			return pearls;
		}
	},
	CRYSTALS("crystals") {
		@Override
		public Currency createPrice(
				long amount) {
			Crystal pearls = new Crystal();
			pearls.set(amount);
			return pearls;
		}
	};

	private static List<OnCurrencyChanged> listeners = new ArrayList<OnCurrencyChanged>();
	private long amount = 0;
	private String externalCurrencyId;

	private Funds(String externalId) {
		externalCurrencyId = externalId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(
			long amount) {
		if (this.amount != amount) {
			this.amount = amount;
			onAmountChanged();
		}
	}

	public void addAmount(
			long amount) {
		this.amount += amount;
		if (amount > 0) {
			onAmountChanged();
		}
	}

	public void substractAmount(
			long amount) {
		long newAmount = Math.max(this.amount - amount, 0);
		if (newAmount != this.amount) {
			this.amount = newAmount;
			onAmountChanged();
		}
	}

	private void onAmountChanged() {
		store();
		for (OnCurrencyChanged listener : listeners) {
			listener.currencyChanged(this);
		}
	}

	public Currency getBalance() {
		return createPrice(amount);
	}

	public abstract Currency createPrice(
			long amount);

	public static void store() {
		List<Holder> data = new ArrayList<Funds.Holder>();
		for (Funds currency : values()) {
			Holder holder = new Holder();
			holder.type = currency.name();
			holder.amount = currency.amount;
			data.add(holder);
		}
		String json = new Gson().toJson(data);
		PreferencesManager.storeFunds(json);
	}

	public static void load() {
		List<Holder> data = null;
		try {
			String json = PreferencesManager.loadFunds();
			data = new Gson().fromJson(json, new TypeToken<ArrayList<Holder>>() {
			}.getType());
		} catch (Exception e) {
			// No data or corrupted.
		}
		if ((data != null) && (data.size() > 0)) {
			for (Holder holder : data) {
				Funds currency = Funds.valueOf(holder.type);
				currency.amount = holder.amount;
			}
		}
	}

	public static final void registerListener(
			OnCurrencyChanged listener) {
		listeners.add(listener);
	}

	public static final void removeListener(
			OnCurrencyChanged listener) {
		listeners.remove(listener);
	}

	private static final class Holder {
		private String type;
		private long amount;
	}

	public static Funds getByExternalCurrencyId(
			String externalCurrencyId) {
		for (Funds currency : values()) {
			if (currency.externalCurrencyId.equals(externalCurrencyId)) {
				return currency;
			}
		}
		return null;
	}

}
