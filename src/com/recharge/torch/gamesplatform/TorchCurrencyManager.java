package com.recharge.torch.gamesplatform;

import java.util.List;
import java.util.Map.Entry;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.recharge.torch.R;
import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Currency;

public class TorchCurrencyManager {
	private static TorchCurrencyManager instance;
	public static final String PREF_KEY = "CurrencyCollection";

	private TorchCurrencyCollection currencies;
	private OnCurrencyChanged currencyChangedListener;

	/**
	 * @param currencyChangedListener
	 *            the currencyChangedListener to set
	 */
	public static void setCurrencyChangedListener(
			OnCurrencyChanged currencyChangedListener) {
		if (instance != null) {
			instance.currencyChangedListener = currencyChangedListener;
		}
	}

	public enum Currencies {
		PEARLS(25, "Pearls", "pearls", R.drawable.ui_funds_pearls), CRYSTALS(26, "Crystals", "crystals", R.drawable.ui_funds_crystal);

		private int currencyId;
		private int iconId;
		private String displayName;
		private String alternateId;

		private Currencies(int id, String alternateId, String displayName, int iconId) {
			currencyId = id;
			this.iconId = iconId;
			this.displayName = displayName;
			this.alternateId = alternateId;
		}

		public int getId() {
			return currencyId;
		}

		public Long getIdLong() {
			return Long.valueOf(currencyId);
		}

		public String getDisplayName() {
			return displayName;
		}

		public int getIconId() {
			return iconId;
		}

		public String getAlternateId() {
			return alternateId;
		}

		public static Currencies getById(
				int id) {
			for (Currencies currency : values()) {
				if (currency.getId() == id) {
					return currency;
				}
			}
			return null;
		}
	}

	private TorchCurrencyManager() {
		currencies = new TorchCurrencyCollection();
	}

	private static void checkInstance() {
		if (instance == null) {
			instance = new TorchCurrencyManager();
		}
	}

	public static void addCurrency(
			Currency currency) {
		checkInstance();
		instance._addCurrency(currency, true);
	}

	private void _addCurrency(
			Currency currency,
			boolean fireListener) {
		_addCurrency(currency, instance.currencies, fireListener);
	}

	private void _addCurrency(
			Currency currency,
			TorchCurrencyCollection collection,
			boolean fireListener) {
		TorchCurrency torchCurrency = currencies.get(currency.getId());
		if (torchCurrency == null) {
			torchCurrency = new TorchCurrency(currency);
		} else {
			torchCurrency.setCurrency(currency);
		}
		_setCurrencyOfflineIcon(torchCurrency);
		collection.put(currency.getId(), torchCurrency);
		if (fireListener) {
			fireListener(torchCurrency);
		}
	}

	private void _setCurrencyOfflineIcon(
			TorchCurrency currency) {
		Currencies currencyType = Currencies.getById(currency.getId());
		if (currencyType != null) {
			currency.setDrawableResource(currencyType.getIconId());
		}
	}

	public static void setCurrencies(
			List<Currency> currencies) {
		checkInstance();
		TorchCurrencyCollection collection = new TorchCurrencyCollection();
		for (Currency currency : currencies) {
			instance._addCurrency(currency, collection, false);
		}
		instance.currencies = collection;
		fireListener(null);
	}

	public static TorchCurrencyCollection getCurrencies() {
		return instance == null ? null : instance.currencies;
	}

	public static void resetCurrencies() {
		checkInstance();
		instance.currencies.clear();
	}

	public static Currency getCurrency(
			Currencies currencyType) {
		checkInstance();
		return instance.currencies.get(currencyType.getIdLong()).getCurrency();
	}

	public static Currency getCurrency(
			long currencyId) {
		checkInstance();
		return instance.currencies.get(currencyId).getCurrency();
	}

	public static void addBalance(
			Currencies currency,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currency.getIdLong());
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.addBalance(balance);
		fireListener(torchCurrency);
	}

	private static void fireListener(
			TorchCurrency currency) {
		if (instance.currencyChangedListener != null) {
			instance.currencyChangedListener.currencyChanged(currency);
		}
	}

	public static void addBalance(
			TorchCurrency currency,
			int balance) {
		currency.addBalance(balance);
		fireListener(currency);
	}

	public static void removeBalance(
			Currencies currency,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currency.getIdLong());
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.removeBalance(balance);
		fireListener(torchCurrency);
	}

	public static void removeBalance(
			TorchCurrency currency,
			int balance) {
		checkInstance();
		currency.removeBalance(balance);
		fireListener(currency);
	}

	public static void setBalance(
			Currencies currency,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currency.getIdLong());
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.setBalance(balance);
		fireListener(torchCurrency);
	}

	public static int getBalance(
			Currencies currency) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currency.getIdLong());
		if (torchCurrency == null) {
			return 0;
		}
		return torchCurrency.getBalance();
	}

	public static void addBalance(
			long currencyId,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currencyId);
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.addBalance(balance);
		fireListener(torchCurrency);
	}

	public static void removeBalance(
			long currencyId,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currencyId);
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.removeBalance(balance);
		fireListener(torchCurrency);
	}

	public static void setBalance(
			long currencyId,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currencyId);
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.setBalance(balance);
		fireListener(torchCurrency);
	}

	public static void setBalance(
			TorchCurrency currency,
			int balance) {
		checkInstance();
		if (currency != null) {
			currency.setBalance(balance);
			fireListener(currency);
		}

	}

	public static int getBalance(
			long currencyId) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currencyId);
		if (torchCurrency == null) {
			return 0;
		}
		return torchCurrency.getBalance();
	}

	public static void storeCurrencies(
			SharedPreferences preferences) {
		checkInstance();
		Editor editor = preferences.edit();
		String json = new Gson().toJson(instance.currencies);
		editor.putString(PREF_KEY, json);
		editor.commit();
	}

	public static void loadCurrencies(
			SharedPreferences preferences) {
		checkInstance();
		try {
			String json = preferences.getString(PREF_KEY, null);
			instance.currencies = new Gson().fromJson(json, TorchCurrencyCollection.class);
		} catch (Exception e) {
			Log.e("TorchCurrencyManager: Exception caught while loading currencies from SharedPreferences", e);
		}
		if (instance.currencies == null) {
			instance.loadDefaultCurrencies();
		}
	}

	private void loadDefaultCurrencies() {
		currencies = new TorchCurrencyCollection();
		for (Currencies currencyType : Currencies.values()) {
			currencies.put(currencyType.getIdLong(), new TorchCurrency(currencyType.getDisplayName(), currencyType.getId(), currencyType.getAlternateId(), currencyType.getIconId(), 0));
		}
	}

	public static void buyItem(
			TorchItem item) {
		checkInstance();
		for (Entry<Long, TorchCurrency> entry : instance.currencies.entrySet()) {
			TorchCurrency currency = entry.getValue();
			Double price = item.getItemPrice(currency.getCurrency());
			if ((price != null) && (price > 0)) {
				currency.removeBalance((int) (price + 0.5));
			}
		}
		fireListener(null);
	}

	public static TorchCurrency findCurrency(
			String currencyName) {
		if (instance == null) {
			return null;
		}
		return instance.currencies.findCurrency(currencyName);
	}

	public static void release() {
		if (instance != null) {
			instance.currencies.clear();
			instance = null;
		}
	}

	public interface OnCurrencyChanged {

		public void currencyChanged(
				TorchCurrency currency);
	}

}
