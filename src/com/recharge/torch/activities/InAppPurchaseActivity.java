package com.recharge.torch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.recharge.torch.gamesplatform.TorchInAppPurchaseManager;
import com.recharge.torch.views.InAppPurchaseList;

public class InAppPurchaseActivity extends Activity {
	private InAppPurchaseList container;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		container = new InAppPurchaseList(this);
		container.loadCategories(TorchInAppPurchaseManager.getCategories());
		addContentView(container, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
	}

}
