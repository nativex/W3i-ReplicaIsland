package com.w3i.replica.replicaisland.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.replica.replicaisland.R;

public class ReplicaIslandToast {

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
}
