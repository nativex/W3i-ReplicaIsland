package com.w3i.torch.views;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.torch.R;
import com.w3i.torch.achivements.Achievement;
import com.w3i.torch.gamesplatform.TorchItem;

public class ReplicaIslandToast {

	public static final int KILLING_SPREE_TEXT_COLOR = Color.YELLOW;
	public static final int KILLING_SPREE_TEXT_SIZE = 14;
	public static final int KILLING_SPREE_KILLS_SIZE = 16;
	public static final int KILLING_SPREE_KILLS_COLOR = Color.BLUE;

	public static Toast makeStoreToast(
			Context context,
			TorchItem item) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastLayout = inflater.inflate(R.layout.store_toast, null);
		toast.setView(toastLayout);
		TextView itemName = (TextView) toastLayout.findViewById(R.id.storeToastItemName);
		CustomImageView itemIcon = (CustomImageView) toastLayout.findViewById(R.id.storeToastIcon);

		toast.setDuration(Toast.LENGTH_SHORT);
		itemName.setText(item.getDisplayName());
		itemIcon.setImageFromInternet(item.getIcon());
		toast.show();
		return toast;
	}

	public static Toast makeKillingSpreeToast(
			Context context,
			int kills,
			int pearlsEarned) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastLayout = inflater.inflate(R.layout.toast_killing_spree, null);
		toast.setView(toastLayout);
		TextView killingSpreeText = (TextView) toastLayout.findViewById(R.id.killingSpreeText);
		String formattedKillingSpreeString = "<font color=orange>" + kills + "<font/><font color=yellow> enemies killed<font/>";
		killingSpreeText.setText(Html.fromHtml(formattedKillingSpreeString));

		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
		toast.show();
		return toast;
	}

	public static Toast makeAchievementDoneToast(
			Context context,
			Achievement achievement) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastLayout = inflater.inflate(R.layout.toast_achievement_done, null);
		toast.setView(toastLayout);

		ImageView icon = (ImageView) toastLayout.findViewById(R.id.toastAchievementIcon);
		TextView title = (TextView) toastLayout.findViewById(R.id.toastAchievementTitle);
		TextView text = (TextView) toastLayout.findViewById(R.id.toastAchievementText);
		TextView name = (TextView) toastLayout.findViewById(R.id.toastAchievementName);

		title.setText(context.getResources().getString(R.string.toast_achievement_done_title));
		text.setText(context.getResources().getString(R.string.toast_achievement_done_text));
		name.setText(achievement.formatText(context, achievement.getNameResource()));
		icon.setImageResource(achievement.getImage());

		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
		toast.show();
		return toast;
	}

	public static Toast makeAchievementUnlockedToast(
			Context context,
			Achievement achievement) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastLayout = inflater.inflate(R.layout.toast_achievement_unlocked, null);
		toast.setView(toastLayout);

		ImageView icon = (ImageView) toastLayout.findViewById(R.id.toastAchievementIcon);
		TextView title = (TextView) toastLayout.findViewById(R.id.toastAchievementTitle);
		TextView text = (TextView) toastLayout.findViewById(R.id.toastAchievementText);
		TextView name = (TextView) toastLayout.findViewById(R.id.toastAchievementName);

		title.setText(context.getResources().getString(R.string.toast_achievement_unlocked_title));
		text.setText(context.getResources().getString(R.string.toast_achievement_unlocked_text));
		name.setText(achievement.formatText(context, achievement.getNameResource()));
		icon.setImageResource(achievement.getImage());

		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
		toast.show();
		return toast;
	}

	public static Toast makeAchievementProgressUpdateToast(
			Context context,
			Achievement achievement,
			int percentDone) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastLayout = inflater.inflate(R.layout.toast_achievement_progress, null);
		toast.setView(toastLayout);

		ImageView icon = (ImageView) toastLayout.findViewById(R.id.toastAchievementIcon);
		TextView title = (TextView) toastLayout.findViewById(R.id.toastAchievementTitle);
		TextView text = (TextView) toastLayout.findViewById(R.id.toastAchievementText);
		TextView name = (TextView) toastLayout.findViewById(R.id.toastAchievementName);

		title.setText(context.getResources().getString(R.string.toast_achievement_progress_title));
		name.setText(achievement.formatText(context, achievement.getNameResource()));
		text.setText("is " + percentDone + "% done");
		icon.setImageResource(achievement.getImage());

		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
		toast.show();
		return toast;
	}
}
