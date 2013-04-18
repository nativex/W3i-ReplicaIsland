package com.recharge.torch.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.recharge.torch.R;
import com.recharge.torch.achivements.Achievement;
import com.recharge.torch.achivements.AchievementListener;
import com.recharge.torch.achivements.AchievementManager;
import com.recharge.torch.achivements.ProgressAchievement;
import com.recharge.torch.utils.MetrixUtils;
import com.recharge.torch.views.ReplicaIslandToast;

public class AchievementsActivity extends Activity {
	private LinearLayout achvContainer;

	private AchievementListener achievementListener = new AchievementListener() {

		@Override
		public void achievementUnlocked(
				final Achievement achievement) {
			final Activity context = AchievementsActivity.this;
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ReplicaIslandToast.makeAchievementUnlockedToast(context, achievement);
				}
			});
		}

		@Override
		public void achievementDone(
				final Achievement achievement) {
			final Activity context = AchievementsActivity.this;
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ReplicaIslandToast.makeAchievementDoneToast(context, achievement);

				}
			});

		}

		@Override
		public void achievementProgressUpdate(
				final Achievement achievement,
				final int percentDone) {
			final Activity context = AchievementsActivity.this;
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ReplicaIslandToast.makeAchievementProgressUpdateToast(context, achievement, percentDone);

				}
			});
		}
	};
	private boolean lockButtons = false;

	@Override
	public boolean onCreateOptionsMenu(
			Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.test_achievements_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuAchievementReset:
			AchievementManager.resetAchievements();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_achievements);

		if (MetrixUtils.getDeviceScreenDiagInches(this) > 6) {
			View scroller = findViewById(R.id.achvMainScroller);
			ViewGroup.LayoutParams params = scroller.getLayoutParams();
			params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600f, getResources().getDisplayMetrics());
		}

		achvContainer = (LinearLayout) findViewById(R.id.achvMainList);
		addAchivements();

		// Keep the volume control type consistent across all activities.
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AchievementManager.registerAchievementListener(achievementListener);
		lockButtons = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		AchievementManager.registerAchievementListener(null);
	}

	private void addAchivements() {
		List<Achievement> achivements = AchievementManager.getAchivements();
		boolean notFirstInList = false;
		for (Achievement a : achivements) {
			if (a.isLocked()) {
				continue;
			}
			if (notFirstInList) {
				addDivider();
			}
			if (a instanceof ProgressAchievement) {
				addProgressAchievement((ProgressAchievement) a);
			} else {
				addAchievement(a);
			}
			notFirstInList = true;
		}
	}

	private void addDivider() {
		ImageView divider = new ImageView(this);
		divider.setBackgroundResource(R.drawable.ui_achievements_activity_list_divider);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 5, 5);
		divider.setLayoutParams(params);
		achvContainer.addView(divider);
	}

	private void addAchievement(
			Achievement achievement) {
		LayoutInflater inflater = getLayoutInflater();
		View achievementLayout = inflater.inflate(R.layout.ui_achievement, null);

		setAchievementParams(achievementLayout, achievement);
	}

	private void addProgressAchievement(
			ProgressAchievement achievement) {
		LayoutInflater inflater = getLayoutInflater();
		View achievementLayout = inflater.inflate(R.layout.ui_achievement_progress, null);

		setAchievementParams(achievementLayout, achievement);

		ProgressBar achievementProgressBar = (ProgressBar) achievementLayout.findViewById(R.id.achvProgress);
		TextView achievementProgressBarText = (TextView) achievementLayout.findViewById(R.id.achvProgressText);

		if (achievement.isDone()) {
			achievementProgressBar.setMax(100);
			achievementProgressBar.setProgress(100);
		} else {
			achievementProgressBar.setMax(achievement.getGoal());
			achievementProgressBar.setProgress(achievement.getProgress());
		}
		achievementProgressBarText.setText(achievement.getProgressString());

	}

	private void setAchievementParams(
			View achievementLayout,
			Achievement achievement) {
		TextView achievementName = (TextView) achievementLayout.findViewById(R.id.uiAchvName);
		TextView achievementDescription = (TextView) achievementLayout.findViewById(R.id.uiAchvDescription);
		ImageView achievementIcon = (ImageView) achievementLayout.findViewById(R.id.uiAchvIcon);

		achievementName.setText(achievement.getName());
		achievementDescription.setText(achievement.getDescription());
		achievementIcon.setImageResource(achievement.getImage());

		achvContainer.addView(achievementLayout);
	}

	@Override
	public boolean onKeyDown(
			int keyCode,
			KeyEvent event) {
		boolean result = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (lockButtons) {
				return result;
			}
			lockButtons = true;
			View achievementsActivity = findViewById(R.id.ui_achievements_activity_container);
			Animation mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
			mFadeOutAnimation.setDuration(500);
			achievementsActivity.startAnimation(mFadeOutAnimation);
			mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(new Intent(this, StartGameActivity.class)));
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
	}

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {

			startActivity(mIntent);
			finish();

			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
			mIntent = null;
		}

		public void onAnimationRepeat(
				Animation animation) {

		}

		public void onAnimationStart(
				Animation animation) {

		}

	}

}