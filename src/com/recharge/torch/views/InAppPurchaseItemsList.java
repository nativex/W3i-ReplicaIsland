package com.recharge.torch.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class InAppPurchaseItemsList extends ScrollView {
	private LinearLayout container;

	public InAppPurchaseItemsList(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InAppPurchaseItemsList(Context context) {
		super(context);
		init();
	}

	private void init() {
		container = new LinearLayout(getContext());
		container.setOrientation(LinearLayout.VERTICAL);

		addView(container, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
	}

	public void addItem(
			View item) {
		container.addView(item);
	}

	public void loadCategory(
			Category category) {
		for (Item item : category.getItems()) {
			InAppPurchaseItem view = new InAppPurchaseItem(getContext());
			view.setItem(item);
			container.addView(view);
		}
	}

	public void reset() {
		container.removeAllViews();
	}

}
