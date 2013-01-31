package com.recharge.torch.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ListLayout extends ScrollView {
	private LinearLayout container;
	public static int ID_CONTAINER = 43444;
	private int deviderResource = -1;

	public ListLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ListLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		container = new LinearLayout(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		container.setLayoutParams(params);
		container.setId(ID_CONTAINER);
		container.setOrientation(LinearLayout.VERTICAL);
	}

	protected ViewGroup getContainer() {
		return container;
	}

	protected void setDevider(
			int devider) {
		deviderResource = devider;
	}

	protected void addItem(
			View item) {
		if ((container.getChildCount() > 0) && (deviderResource > 0)) {
			addDevider();
		}
		container.addView(item);
	}

	private void addDevider() {
		ImageView divider = new ImageView(getContext());
		divider.setBackgroundResource(deviderResource);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 5, 5);
		divider.setLayoutParams(params);
		addView(divider);
	}

	public void release() {
		if (container != null) {
			for (int i = 0; i < container.getChildCount(); i++) {
				View v = container.getChildAt(i);
				v.setBackgroundDrawable(null);
				v.setOnClickListener(null);
			}
			container.removeAllViews();
		}
	}

}
