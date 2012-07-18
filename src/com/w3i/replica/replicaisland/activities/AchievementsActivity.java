package com.w3i.replica.replicaisland.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.achivements.Achievement;
import com.w3i.replica.replicaisland.achivements.AchievementListener;
import com.w3i.replica.replicaisland.achivements.AchievementManager;
import com.w3i.replica.replicaisland.achivements.ProgressAchievement;
import com.w3i.replica.replicaisland.store.ReplicaIslandToast;

public class AchievementsActivity extends Activity {
	private LinearLayout achvContainer;

	private AchievementListener achievementListener = new AchievementListener() {

		@Override
		public void achievementUnlocked(
				Achievement achievement) {
			ReplicaIslandToast.makeAchievementUnlockedToast(AchievementsActivity.this, achievement).show();
		}

		@Override
		public void achievementDone(
				Achievement achievement) {
			ReplicaIslandToast.makeAchievementDoneToast(AchievementsActivity.this, achievement).show();

		}
	};

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
		setContentView(R.layout.ui_achivements_activity);

		achvContainer = (LinearLayout) findViewById(R.id.achvMainList);
		addAchivements();

	}

	@Override
	protected void onResume() {
		super.onResume();
		AchievementManager.registerAchievementListener(achievementListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AchievementManager.registerAchievementListener(null);
	}

	private void addAchivements() {
		List<Achievement> achivements = AchievementManager.getAchivements();
		for (Achievement a : achivements) {
			if (a.isLocked()) {
				continue;
			}
			if (a instanceof ProgressAchievement) {
				addProgressAchievement((ProgressAchievement) a);
			} else {
				addAchievement(a);
			}
		}
	}

	private void addAchievement(
			Achievement achievement) {
		LayoutInflater inflater = getLayoutInflater();
		View achievementLayout = inflater.inflate(R.layout.ui_achivement, null);

		setAchievementParams(achievementLayout, achievement);
	}

	private void addProgressAchievement(
			ProgressAchievement achievement) {
		LayoutInflater inflater = getLayoutInflater();
		View achievementLayout = inflater.inflate(R.layout.ui_achivement_progress, null);

		setAchievementParams(achievementLayout, achievement);

		ProgressBar achievementProgressBar = (ProgressBar) achievementLayout.findViewById(R.id.achvProgress);
		TextView achievementProgressBarText = (TextView) achievementLayout.findViewById(R.id.achvProgressText);

		achievementProgressBar.setMax(achievement.getGoal());
		achievementProgressBar.setProgress(achievement.getProgress());
		achievementProgressBarText.setText(achievement.getProgressString());

	}

	private void setAchievementParams(
			View achievementLayout,
			Achievement achievement) {
		TextView achievementName = (TextView) achievementLayout.findViewById(R.id.uiAchvName);
		TextView achievementDescription = (TextView) achievementLayout.findViewById(R.id.uiAchvDescription);
		// ImageView achievementIcon = (ImageView) achievementLayout.findViewById(R.id.uiAchvIcon);

		achievementName.setText(achievement.getName());
		achievementDescription.setText(achievement.getDescription());
		// achievementIcon.setImageResource(achievement.getImage());
		// achievementIcon.setVisibility(View.GONE);

		achvContainer.addView(achievementLayout);
	}

}
