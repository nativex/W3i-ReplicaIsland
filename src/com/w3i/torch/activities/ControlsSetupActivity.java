package com.w3i.torch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.w3i.torch.R;
import com.w3i.torch.utils.MetrixUtils;

public class ControlsSetupActivity extends Activity {

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float diagonalInches = MetrixUtils.getDeviceScreenDiagInches(this);
		setContentView(R.layout.ui_activity_controls);
		if (diagonalInches > 6) {
			View scroller = findViewById(R.id.ui_controls_scroller);
			RelativeLayout.LayoutParams params = (LayoutParams) scroller.getLayoutParams();
			params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600f, getResources().getDisplayMetrics());
			// scroller.setLayoutParams(params);
		}

	}

}
