package com.recharge.torch.views;

import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recharge.torch.R;
import com.recharge.torch.gamesplatform.TorchCurrency;
import com.recharge.torch.gamesplatform.TorchCurrencyCollection;
import com.recharge.torch.gamesplatform.TorchCurrencyManager;
import com.recharge.torch.gamesplatform.TorchCurrencyManager.OnCurrencyChanged;
import com.w3i.advertiser.NetworkConnectionManager;
import com.w3i.offerwall.PublisherManager;
import com.w3i.offerwall.custom.views.CustomImageView;

public class FundsView {
	public static ViewGroup fundsView;
	public static final int ID_PLUS_BUTTON = 800;

	private static OnCurrencyChanged listener = null;
	private static OnCurrencyChanged internalListener = new OnCurrencyChanged() {

		@Override
		public void currencyChanged(
				TorchCurrency currency) {
			if (fundsView != null) {
				if (currency != null) {
					setFunds(currency, getFundsListView());
				} else {
					setFunds();
				}
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
			TorchCurrencyManager.setCurrencyChangedListener(internalListener);
			FundsView.fundsView = fundsView;
			FundsView.fundsView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(
						View v) {
					com.w3i.common.Log.d("FundsView: Funds view clicked");
					PublisherManager.showIncentOfferWall();
				}
			});
			setFunds();
		} catch (Exception e) {
			Log.e("ReplicaIsland", "FundsView: Unexpected exception caught while displaying the resources.", e);
		}
		return fundsView;
	}

	public static void setFunds(
			TorchCurrency currency,
			ViewGroup fundsList) {
		if (fundsView != null) {
			int itemId = currency.getId() + 10000;
			ViewGroup fundsItem = (ViewGroup) fundsList.findViewById(itemId);
			if (fundsItem == null) {
				fundsItem = createFundsItem(fundsView.getContext(), currency, itemId);
				fundsList.addView(fundsItem);
			}
			setFunds(fundsItem, currency);
		}
		if (listener != null) {
			listener.currencyChanged(currency);
		}
	}

	public static void setFunds() {
		if (fundsView != null) {
			TorchCurrencyCollection collection = TorchCurrencyManager.getCurrencies();

			for (Entry<Long, TorchCurrency> entry : collection.entrySet()) {
				TorchCurrency currency = entry.getValue();
				setFunds(currency, getFundsListView());
			}
			createPlusButton();
		}
	}

	private static ViewGroup getFundsListView() {
		if (fundsView != null) {
			final ViewGroup fundsList = (ViewGroup) fundsView.findViewById(R.id.uiFundsList);

			fundsList.post(new Runnable() {

				@Override
				public void run() {
					fundsList.setBackgroundResource(R.drawable.ui_funds_list_box_with_plus_bg);
					fundsList.setPadding(2, 2, 0, 2);

					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 0, 0, 0);

					fundsList.setLayoutParams(params);
				}
			});
			return fundsList;
		}
		return null;
	}

	private static void createPlusButton() {
		CustomImageView plusImage = (CustomImageView) fundsView.findViewById(ID_PLUS_BUTTON);

		if (plusImage == null) {
			plusImage = new CustomImageView(fundsView.getContext());
			plusImage.setImageResource(R.drawable.ui_funds_plus);
			plusImage.setBackgroundResource(R.drawable.ui_funds_below_plus_bg);
			plusImage.setPadding(0, 0, 0, 0);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF, R.id.uiFundsList);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.uiFundsList);
			params.setMargins(0, 0, 0, 0);
			plusImage.setLayoutParams(params);
			plusImage.setId(ID_PLUS_BUTTON);
			fundsView.addView(plusImage);
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
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(4, 4, 10, 4);
		params.gravity = Gravity.CENTER_VERTICAL;
		fundsItem.setLayoutParams(params);
		fundsItem.setId(itemId);
		return fundsItem;
	}

	private static void setFunds(
			final ViewGroup fundsItem,
			final TorchCurrency currency) {
		fundsView.post(new Runnable() {

			@Override
			public void run() {
				if (fundsItem == null) {
					return;
				}
				CustomImageView icon = (CustomImageView) fundsItem.findViewById(R.id.uiFundsItemImage);
				TextView amount = (TextView) fundsItem.findViewById(R.id.uiFundsItemAmount);
				amount.setText(String.format("%1$,d", currency.getBalance()));
				if ((NetworkConnectionManager.getInstance(fundsItem.getContext()).isConnected()) && (currency.getIcon() != null)) {
					icon.setImageFromInternet(currency.getIcon());
				} else if (currency.getDrawableResource() > 0) {
					icon.setImageResource(currency.getDrawableResource());
				}
			}
		});
	}

	public static void setOnCurrencyChangedListener(
			OnCurrencyChanged listener) {
		FundsView.listener = listener;
	}

	public static void releaseFunds() {
		TorchCurrencyManager.setCurrencyChangedListener(null);
		fundsView = null;
		listener = null;
	}

}
