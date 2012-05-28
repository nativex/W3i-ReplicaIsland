package com.w3i.replica.replicaisland.store;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.AdvancedTextView;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.replica.replicaisland.R;

public class ReplicaIslandToast {

	public static final int KILLING_SPREE_TEXT_COLOR = Color.YELLOW;
	public static final int KILLING_SPREE_TEXT_SIZE = 14;
	public static final int KILLING_SPREE_KILLS_SIZE = 16;
	public static final int KILLING_SPREE_KILLS_COLOR = Color.BLUE;

	public static Toast makeStoreToast(
			Context context,
			Item item) {
		Toast toast = new Toast(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastLayout = inflater.inflate(R.layout.store_toast, null);
		toast.setView(toastLayout);
		TextView itemName = (TextView) toastLayout.findViewById(R.id.storeToastItemName);
		CustomImageView itemIcon = (CustomImageView) toastLayout.findViewById(R.id.storeToastIcon);

		toast.setDuration(Toast.LENGTH_SHORT);
		itemName.setText(item.getDisplayName());
		itemIcon.setImageFromInternet(item.getStoreImageUrl());
		return toast;
	}

	public static Toast makeKillingSpreeToast(
			Context context,
			int kills,
			int pearlsEarned) {
		Toast toast = new Toast(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastLayout = inflater.inflate(R.layout.toast_killing_spree, null);
		toast.setView(toastLayout);
		AdvancedTextView killingSpreeText = (AdvancedTextView) toastLayout.findViewById(R.id.killingSpreeText);

		AdvancedTextView.Text text = killingSpreeText.new Text();
		text.setText("You destroyed ");
		text.setColor(KILLING_SPREE_TEXT_COLOR);
		text.setSize(KILLING_SPREE_TEXT_SIZE);
		killingSpreeText.addText(text);

		text = killingSpreeText.new Text();
		text.setText(Integer.toString(kills));
		text.setColor(KILLING_SPREE_KILLS_COLOR);
		text.setSize(KILLING_SPREE_KILLS_SIZE);
		killingSpreeText.addText(text);

		text = killingSpreeText.new Text();
		text.setText(" enemies in a row(" + pearlsEarned + ").");
		text.setColor(KILLING_SPREE_TEXT_COLOR);
		text.setSize(KILLING_SPREE_TEXT_SIZE);
		killingSpreeText.addText(text);

		toast.setDuration(Toast.LENGTH_SHORT);
		return toast;
	}
}
