package com.w3i.torch.activities;

import com.w3i.offerwall.PublisherManager;

import android.app.Activity;
import android.os.Bundle;

public class CreditsActivity extends Activity {

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
