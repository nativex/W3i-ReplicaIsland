package com.recharge.torch.views;

import android.content.ClipData.Item;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.recharge.torch.R;

public class InAppPurchaseItemsList extends ScrollView {
	private LinearLayout container;
	private View.OnClickListener onItemClickListener = null;

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
		setBackgroundResource(R.drawable.ui_iap_categories_container_background);

		addView(container, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
	}

	public void addItem(
			View item) {
		item.setOnClickListener(onItemClickListener);
		container.addView(item);
	}

	public void loadCategory(
			Category category) {
		if (container.getChildCount() > category.getItems().size()) {
			for (int i = category.getItems().size(); i < container.getChildCount(); i++) {
				container.getChildAt(i).setVisibility(View.GONE);
			}
		} else if (container.getChildCount() < category.getItems().size()) {
			for (int i = container.getChildCount(); i < category.getItems().size(); i++) {
				container.addView(new InAppPurchaseItem(getContext()));
			}
		}
		for (int i = 0; i < category.getItems().size(); i++) {
			Item item = category.getItems().get(i);
			InAppPurchaseItem view = (InAppPurchaseItem) container.getChildAt(i);
			view.setVisibility(View.VISIBLE);
			view.setItem(item);
			view.setOnClickListener(onItemClickListener);
		}
	}

	public void setOnItemClickListener(
			View.OnClickListener listener) {
		onItemClickListener = listener;
		if (container.getChildCount() > 0) {
			for (int i = 0; i < container.getChildCount(); i++) {
				getChildAt(i).setOnClickListener(listener);
			}
		}
	}

	public void reset() {
		container.removeAllViews();
	}

}
