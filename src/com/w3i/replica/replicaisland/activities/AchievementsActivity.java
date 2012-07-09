package com.w3i.replica.replicaisland.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.achivements.Achievement;
import com.w3i.replica.replicaisland.achivements.AchievementManager;
import com.w3i.replica.replicaisland.achivements.ProgressAchievement;

public class AchievementsActivity extends Activity {
	private LinearLayout achvContainer;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_achivements_activity);

		achvContainer = (LinearLayout) findViewById(R.id.achvMainList);
		addAchivements();

	}

	private void addAchivements() {
		List<Achievement> achivements = AchievementManager.getAchivements();
		for (Achievement a : achivements) {
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
