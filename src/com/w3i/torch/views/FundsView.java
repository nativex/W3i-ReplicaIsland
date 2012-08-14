package com.w3i.torch.views;

import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.w3i.advertiser.NetworkConnectionManager;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.torch.R;
import com.w3i.torch.gamesplatform.TorchCurrency;
import com.w3i.torch.gamesplatform.TorchCurrencyCollection;
import com.w3i.torch.gamesplatform.TorchCurrencyManager;
import com.w3i.torch.gamesplatform.TorchCurrencyManager.OnCurrencyChanged;

public class FundsView {
	public static ViewGroup fundsView;

	private static OnCurrencyChanged listener = new OnCurrencyChanged() {

		@Override
		public void currencyChanged(
				TorchCurrency currency) {
			if (currency != null) {
				setFunds(currency);
			} else {
				setFunds();
			}
		}
	};

	public static ViewGroup setFunds(
			Activity activity,
			ViewGroup fundsView) {
		try {
			if (fundsView == null) {
				fundsView = createFundsView(activity);
			}
			TorchCurrencyManager.setCurrencyChangedListener(listener);
			FundsView.fundsView = fundsView;
			setFunds();
		} catch (Exception e) {
			Log.e("ReplicaIsland", "FundsView: Unexpected exception caught while displaying the resources.", e);
		}
		return fundsView;
	}

	public static void setFunds(
			TorchCurrency currency) {
		if (fundsView != null) {
			int itemId = currency.getId() * 1000;
			ViewGroup fundsItem = (ViewGroup) fundsView.findViewById(itemId);
			if (fundsItem == null) {
				fundsItem = createFundsItem(fundsView.getContext(), currency, itemId);
				fundsView.addView(fundsItem);
			}
			setFunds(fundsItem, currency);
		}
	}

	public static void setFunds() {
		if (fundsView != null) {
			TorchCurrencyCollection collection = TorchCurrencyManager.getCurrencies();
			for (Entry<Long, TorchCurrency> entry : collection.entrySet()) {
				TorchCurrency currency = entry.getValue();
				setFunds(currency);
			}
		}
	}

	private static ViewGroup createFundsView(
			Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup fundsLayout = (ViewGroup) inflater.inflate(R.layout.ui_funds_list, null);
		fundsLayout.setMinimumWidth(150);
		return fundsLayout;
	}

	private static ViewGroup createFundsItem(
			Context context,
			TorchCurrency currency,
			int itemId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup fundsItem = (ViewGroup) inflater.inflate(R.layout.ui_funds_item, null);
		fundsItem.setId(itemId);
		return fundsItem;
	}

	private static void setFunds(
			ViewGroup fundsItem,
			TorchCurrency currency) {
		if (fundsItem == null) {
			return;
		}
		CustomImageView icon = (CustomImageView) fundsItem.findViewById(R.id.uiFundsItemImage);
		TextView amount = (TextView) fundsItem.findViewById(R.id.uiFundsItemAmount);
		amount.setText(Integer.toString(currency.getBalance()));
		if ((NetworkConnectionManager.getInstance(fundsItem.getContext()).isConnected()) && (currency.getIcon() != null)) {
			icon.setImageFromInternet(currency.getIcon());
		} else if (currency.getDrawableResource() > 0) {
			icon.setImageResource(currency.getDrawableResource());
		}
	}

	public static void releaseFunds() {
		fundsView = null;
	}

}
