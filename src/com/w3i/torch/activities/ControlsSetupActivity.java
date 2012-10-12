package com.w3i.torch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.w3i.torch.PreferenceConstants;
import com.w3i.torch.R;
import com.w3i.torch.utils.MetrixUtils;
import com.w3i.torch.views.Option;
import com.w3i.torch.views.OptionsCheckbox;
import com.w3i.torch.views.OptionsSlider;
import com.w3i.torch.views.TorchCheckbox;
import com.w3i.torch.views.TorchCheckbox.State;

public class ControlsSetupActivity extends Activity {
	private OptionsCheckbox clickAttack;
	private Option keyConfig;
	private OptionsSlider movementSensitivity;
	private OptionsCheckbox enableScreenControls;
	private OptionsCheckbox enableTiltControls;
	private OptionsSlider tiltControlsSensitivity;
	private SharedPreferences preferences;

	private OptionsSlider.OnSeekbarPositionChange onSeekbarChange = new OptionsSlider.OnSeekbarPositionChange() {

		@Override
		public void onPositionChange(
				OptionsSlider slider,
				int position) {
			if (slider == movementSensitivity) {
				Editor edit = preferences.edit();
				edit.putInt(PreferenceConstants.PREFERENCE_MOVEMENT_SENSITIVITY, position);
				edit.commit();
			} else if (slider == tiltControlsSensitivity) {
				Editor edit = preferences.edit();
				edit.putInt(PreferenceConstants.PREFERENCE_TILT_SENSITIVITY, position);
				edit.commit();
			}
		}
	};
	private TorchCheckbox.OnCheckboxStateChanged onCheckboxStateListener = new TorchCheckbox.OnCheckboxStateChanged() {

		@Override
		public void checkboxStateChanged(
				View checkbox,
				State state) {
			if (checkbox.getId() == clickAttack.getId()) {
				Editor edit = preferences.edit();
				edit.putBoolean(PreferenceConstants.PREFERENCE_CLICK_ATTACK, state == State.CHECKED ? true : false);
				edit.commit();
			} else if (checkbox.getId() == enableScreenControls.getId()) {
				if (state == State.CHECKED) {
					Editor edit = preferences.edit();
					edit.putBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, true);
					edit.putBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, false);
					edit.commit();
					enableTiltControls.disable();
					tiltControlsSensitivity.disable();
				} else {
					Editor edit = preferences.edit();
					edit.putBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, false);
					edit.commit();
					enableTiltControls.enable();
					boolean titlControlsEnabled = preferences.getBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, false);
					enableTiltControls.getCheckbox().setState(titlControlsEnabled ? State.CHECKED : State.UNCHECKED);
					if (titlControlsEnabled) {
						tiltControlsSensitivity.enable();
					} else {
						tiltControlsSensitivity.disable();
					}
				}
			} else if (checkbox.getId() == enableTiltControls.getId()) {
				Editor edit = preferences.edit();
				edit.putBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, state == State.CHECKED ? true : false);
				edit.commit();
				if (state == State.CHECKED) {
					tiltControlsSensitivity.enable();
				} else {
					tiltControlsSensitivity.disable();
				}
			}
		}
	};

	private View.OnClickListener onOptionListener = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			// TODO controls setup dialog.
		}
	};

	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float diagonalInches = MetrixUtils.getDeviceScreenDiagInches(this);
		setContentView(R.layout.ui_activity_controls);
		if (diagonalInches > 6) {
			View scroller = findViewById(R.id.ui_controls_scroller);
			RelativeLayout.LayoutParams params = (LayoutParams) scroller.getLayoutParams();
			params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600f, getResources().getDisplayMetrics());
		}

		preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);

		clickAttack = (OptionsCheckbox) findViewById(R.id.ui_controls_click_attack);
		keyConfig = (Option) findViewById(R.id.ui_controls_key_config);
		movementSensitivity = (OptionsSlider) findViewById(R.id.ui_controls_movement_sensitivity);
		enableScreenControls = (OptionsCheckbox) findViewById(R.id.ui_controls_enable_screen_controls);
		enableTiltControls = (OptionsCheckbox) findViewById(R.id.ui_controls_enable_tilt_controls);
		tiltControlsSensitivity = (OptionsSlider) findViewById(R.id.ui_controls_tilt_sensitivity);

		boolean clickAttackEnabled = preferences.getBoolean(PreferenceConstants.PREFERENCE_CLICK_ATTACK, false);
		clickAttack.getCheckbox().setState(clickAttackEnabled ? State.CHECKED : State.UNCHECKED);
		clickAttack.setOnCheckboxStateListener(onCheckboxStateListener);

		boolean screenControlsEnabled = preferences.getBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, false);
		enableScreenControls.getCheckbox().setState(screenControlsEnabled ? State.CHECKED : State.UNCHECKED);
		enableScreenControls.setOnCheckboxStateListener(onCheckboxStateListener);

		boolean titlControlsEnabled = preferences.getBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, false);
		enableTiltControls.getCheckbox().setState(titlControlsEnabled ? State.CHECKED : State.UNCHECKED);
		enableTiltControls.setOnCheckboxStateListener(onCheckboxStateListener);

		keyConfig.setOnClickListener(onOptionListener);

		movementSensitivity.setOnSeekBarChanged(onSeekbarChange);
		int movementSensitivityValue = preferences.getInt(PreferenceConstants.PREFERENCE_MOVEMENT_SENSITIVITY, 100);
		movementSensitivity.setSeekbarMax(100);
		movementSensitivity.setSeekbarPos(movementSensitivityValue);

		tiltControlsSensitivity.setOnSeekBarChanged(onSeekbarChange);
		int tiltSensitivityValue = preferences.getInt(PreferenceConstants.PREFERENCE_TILT_SENSITIVITY, 100);
		tiltControlsSensitivity.setSeekbarMax(100);
		tiltControlsSensitivity.setSeekbarPos(tiltSensitivityValue);

		if (screenControlsEnabled) {
			enableTiltControls.disable();
			titlControlsEnabled = false;
			Editor edit = preferences.edit();
			edit.putBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, false);
			edit.commit();
		}

		if (!titlControlsEnabled) {
			tiltControlsSensitivity.disable();
		}
	}
}
