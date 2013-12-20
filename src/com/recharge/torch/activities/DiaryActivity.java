/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.recharge.torch.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.recharge.torch.BaseObject;
import com.recharge.torch.R;
import com.recharge.torch.achivements.Achievement.State;
import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementData;
import com.recharge.torch.achivements.AchievementManager;

public class DiaryActivity extends Activity {

	private View.OnClickListener mKillDiaryListener = new View.OnClickListener() {
		public void onClick(
				View arg0) {
			finish();
			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
		}
	};

	@Override
	public void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_diary);

		TextView text = (TextView) findViewById(R.id.diarytext);

		ImageView image = (ImageView) findViewById(R.id.diarybackground);
		image.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade));
		final Intent callingIntent = getIntent();
		final int textResource = callingIntent.getIntExtra("text", -1);
		AchievementManager.setAchievementState(Type.DIARIES, State.UPDATE, new AchievementData<Integer>(textResource));

		if (textResource != -1) {
			text.setText(textResource);
		}

		ImageView okArrow = (ImageView) findViewById(R.id.ok);
		okArrow.setOnClickListener(mKillDiaryListener);
		okArrow.setBackgroundResource(R.anim.ui_button);
		AnimationDrawable anim = (AnimationDrawable) okArrow.getBackground();
		anim.start();

		BaseObject.getSystemRegistry().customToastSystem.toast(getString(R.string.diary_found), Toast.LENGTH_SHORT);

	}

}
