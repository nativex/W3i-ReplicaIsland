package com.w3i.torch.gamesplatform;

import com.w3i.gamesplatformsdk.rest.entities.Currency;

public class TorchCurrency {
	private Currency gamesPlatformCurrency;
	private int balance;

	public TorchCurrency(Currency currency) {
		gamesPlatformCurrency = currency;
	}

	public Currency getCurrency() {
		return gamesPlatformCurrency;
	}

	public void setBalance(
			int balance) {
		this.balance = balance;
	}

	public int getBalance() {
		return balance;
	}

	public void addBalance(
			int balance) {
		this.balance += balance;
	}

	public String getDisplayName() {
		return gamesPlatformCurrency.getDisplayName();
	}

	public void removeBalance(
			int balance) {
		this.balance -= balance;
	}

	public String getIcon() {
		return gamesPlatformCurrency.getStoreImageUrl();
	}

}
