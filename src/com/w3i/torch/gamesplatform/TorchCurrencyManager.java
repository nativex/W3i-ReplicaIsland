package com.w3i.torch.gamesplatform;

import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Currency;

public class TorchCurrencyManager {
	private static TorchCurrencyManager instance;
	public static final String PREF_KEY = "CurrencyCollection";

	private TorchCurrencyCollection currencies;

	public enum Currencies {
		PEARLS(25),
		CRYSTALS(26);

		private int currencyId;

		private Currencies(int id) {
			currencyId = id;
		}

		public int getId() {
			return currencyId;
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
		instance._addCurrency(currency);
	}

	private void _addCurrency(
			Currency currency) {
		currencies.put(currency.getId(), new TorchCurrency(currency));
	}

	public static void setCurrencies(
			List<Currency> currencies) {
		checkInstance();
		for (Currency currency : currencies) {
			instance._addCurrency(currency);
		}
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
		return instance.currencies.get(currencyType.getId()).getCurrency();
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
		TorchCurrency torchCurrency = instance.currencies.get(currency.getId());
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.addBalance(balance);
	}

	public static void removeBalance(
			Currencies currency,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currency.getId());
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.removeBalance(balance);
	}

	public static void setBalance(
			Currencies currency,
			int balance) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currency.getId());
		if (torchCurrency == null) {
			return;
		}
		torchCurrency.setBalance(balance);
	}

	public static int getBalance(
			Currencies currency) {
		checkInstance();
		TorchCurrency torchCurrency = instance.currencies.get(currency.getId());
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
	}

	public static void release() {
		if (instance != null) {
			instance.currencies.clear();
			instance = null;
		}
	}

}
