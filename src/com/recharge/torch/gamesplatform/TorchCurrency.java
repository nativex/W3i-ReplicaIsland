package com.recharge.torch.gamesplatform;

import com.recharge.torch.R;
import com.w3i.gamesplatformsdk.rest.entities.Currency;

public class TorchCurrency {
	private Currency gamesPlatformCurrency;
	private int balance;
	private int drawableResource = R.drawable.ui_funds_unknown;
	private int id;
	private String displayName;
	private String alternateId;

	/**
	 * @return the drawableResource
	 */
	public int getDrawableResource() {
		return drawableResource;
	}

	/**
	 * @param drawableResource
	 *            the drawableResource to set
	 */
	public void setDrawableResource(
			int drawableResource) {
		this.drawableResource = drawableResource;
	}

	public TorchCurrency(Currency currency) {
		setCurrency(currency);
		this.displayName = currency.getDisplayName();
		this.id = currency.getId().intValue();
		this.alternateId = currency.getAlternateId();
	}

	public TorchCurrency(TorchCurrency torchCurrency, Currency currency) {
		this(currency);
		if (torchCurrency != null) {
			balance = torchCurrency.balance;
			drawableResource = torchCurrency.drawableResource;
		}
	}

	public TorchCurrency(String displayName, int id, String alternativeId, int icon, int balance) {
		this.displayName = displayName;
		this.id = id;
		this.drawableResource = icon;
		this.balance = balance;
		this.alternateId = alternativeId;
	}

	public void setCurrency(
			Currency currency) {
		this.id = currency.getId().intValue();
		this.displayName = currency.getDisplayName();
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

	public Double getBalanceDouble() {
		if (balance <= 0) {
			return null;
		}
		return Double.valueOf(balance);
	}

	public void addBalance(
			int balance) {
		this.balance += balance;
	}

	public int getId() {
		if (gamesPlatformCurrency != null) {
			return gamesPlatformCurrency.getId().intValue();
		}
		return id;
	}

	public String getDisplayName() {
		if (gamesPlatformCurrency != null) {
			return gamesPlatformCurrency.getDisplayName();
		}
		return displayName;
	}

	public String getAlternateId() {
		if (gamesPlatformCurrency != null) {
			return gamesPlatformCurrency.getAlternateId();
		}
		return alternateId;
	}

	public void removeBalance(
			int balance) {
		this.balance -= balance;
	}

	public String getIcon() {
		if (gamesPlatformCurrency != null) {
			return gamesPlatformCurrency.getStoreImageUrl();
		}
		return null;
	}

}
