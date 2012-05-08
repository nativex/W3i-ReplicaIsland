package com.w3i.replica.replicaisland.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.w3i.gamesplatformsdk.Log;
import com.w3i.replica.replicaisland.R;

public class StoreActivity extends Activity {
	private ReplicaStoreManager storeManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup mainLayout = (ViewGroup) inflater.inflate(
				R.layout.store_layout, null);
		setContentView(mainLayout);
		LinearLayout itemsList = (LinearLayout) mainLayout
				.findViewById(R.id.storeItemsList);
		GridView historyList = (GridView) mainLayout
				.findViewById(R.id.historyItems);
		if (itemsList == null) {
			Log.e("StoreActivity: cannot find Items list");
		}
		if (historyList == null) {
			Log.e("StoreActivity: cannot find History List");
		}
		storeManager = new ReplicaStoreManager(itemsList, historyList);
		setupScrollerTextView();

	}

	private void setupScrollerTextView() {
		View v = findViewById(R.id.storeScrollHistory);
		Animation a = AnimationUtils.loadAnimation(this,
				R.anim.store_to_history_label);
		v.setAnimation(a);
		a.setFillAfter(true);
		a.start();

		v = findViewById(R.id.storeScrollStore);
		a = AnimationUtils.loadAnimation(this, R.anim.store_to_store_label);
		v.setAnimation(a);
		a.setFillAfter(true);
		a.start();
	}

	@Override
	protected void onResume() {
		addItemsToStoreManager();
		super.onResume();
	}

	private void addItemsToStoreManager() {

	}
}
