package com.w3i.replica.replicaisland.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.w3i.gamesplatformsdk.Log;
import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.store.VerticalTextView.ORIENTATION;

public class StoreActivity extends Activity {
	private ReplicaStoreManager storeManager;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup mainLayout = (ViewGroup) inflater.inflate(R.layout.store_layout, null);
		setContentView(mainLayout);
		LinearLayout itemsList = (LinearLayout) mainLayout.findViewById(R.id.storeItemsList);
		GridView historyList = (GridView) mainLayout.findViewById(R.id.historyItems);
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
		VerticalTextView v = (VerticalTextView) findViewById(R.id.storeScrollHistory);
		v.setOrientation(ORIENTATION.LEFT);
		v.setText("Store");

		v = (VerticalTextView) findViewById(R.id.storeScrollStore);
		v.setOrientation(ORIENTATION.RIGHT);
		v.setText("Purchased Items");
	}

	@Override
	protected void onResume() {
		addItemsToStoreManager();
		super.onResume();
	}

	private void addItemsToStoreManager() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		storeManager.release();
	}
}
