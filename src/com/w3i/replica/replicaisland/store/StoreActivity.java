package com.w3i.replica.replicaisland.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.w3i.replica.replicaisland.R;

public class StoreActivity extends Activity {
	private StoreListManager storeManager;

	private View.OnClickListener onItemClick = new View.OnClickListener() {

		@Override
		public void onClick(View itemLayout) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup mainLayout = (ViewGroup) inflater.inflate(
				R.layout.store_layout, null);
		setContentView(mainLayout);
		LinearLayout itemsList = (LinearLayout) mainLayout
				.findViewById(R.id.itemsList);
		storeManager = new StoreListManager(itemsList);

	}

	@Override
	protected void onResume() {
		addItemsToStoreManager();
		super.onResume();
	}

	private void addItemsToStoreManager() {

	}
}
