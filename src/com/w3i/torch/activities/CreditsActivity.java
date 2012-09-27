package com.w3i.torch.activities;

import android.app.Activity;
import android.os.Bundle;

import com.w3i.offerwall.PublisherManager;
import com.w3i.torch.R;

public class CreditsActivity extends Activity {

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_credits);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PublisherManager.createSession();
	}

	@Override
	protected void onPause() {
		super.onPause();
		PublisherManager.endSession();
	}
}
