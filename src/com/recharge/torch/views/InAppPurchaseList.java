package com.recharge.torch.views;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nativex.monetization.custom.views.CustomImageView;
import com.recharge.torch.R;
import com.recharge.torch.views.InAppPurchaseCategoryScroller.OnCategoryChanged;
import com.w3i.gamesplatformsdk.rest.entities.Category;

public class InAppPurchaseList extends ViewGroup {
	private InAppPurchaseCategoryScroller scroller;
	private InAppPurchaseItemsList list;
	private ImageView arrowUp;
	private ImageView arrowDown;
	private List<Category> categories;

	public InAppPurchaseList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public InAppPurchaseList(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InAppPurchaseList(Context context) {
		super(context);
		init();
	}

	private void init() {
		scroller = new InAppPurchaseCategoryScroller(getContext());
		list = new InAppPurchaseItemsList(getContext());
		arrowUp = new ImageView(getContext());
		arrowDown = new ImageView(getContext());
		setWillNotDraw(false);

		arrowUp.setImageResource(R.drawable.ui_iap_arrow_up);
		arrowDown.setImageResource(R.drawable.ui_iap_arrow_down);

		addView(arrowUp);
		addView(arrowDown);
		addView(scroller);
		addView(list);
	}

	private OnCategoryChanged categoryListener = new OnCategoryChanged() {

		@Override
		public void onChanged(
				int index) {
			if (categories != null) {
				if ((categories.size() < index) && (index > 0)) {
					Category active = categories.get(index);
					list.loadCategory(active);
				}
			}
		}
	};

	public void loadCategories(
			List<Category> categories) {
		if (this.categories != categories) {
			this.categories = categories;
			scroller.reset();
			list.reset();
			scroller.setOnCategoryChangedListener(categoryListener);
			if ((categories != null) && (categories.size() > 0)) {
				for (Category category : categories) {
					CustomImageView image = new CustomImageView(getContext());
					ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(80, 80);
					image.setLayoutParams(params);
					image.setImageFromInternet(category.getStoreImageUrl());
					image.setBackgroundColor(Color.RED);
					scroller.addItem(image);
				}
			}
		}
	}

	@Override
	protected void onLayout(
			boolean changed,
			int l,
			int t,
			int r,
			int b) {
		int left, right, bottom, top;
		left = l + (scroller.getMeasuredWidth() - arrowUp.getMeasuredWidth()) / 2;
		bottom = t + arrowUp.getMeasuredHeight();
		arrowUp.layout(left, t, left + arrowUp.getMeasuredWidth(), t + arrowUp.getMeasuredHeight());

		top = bottom;
		left = l;
		right = left + scroller.getMeasuredWidth();
		bottom = top + scroller.getMeasuredHeight();
		scroller.layout(left, top, right, bottom);

		left = l + (scroller.getMeasuredWidth() - arrowDown.getMeasuredWidth()) / 2;
		top = bottom;
		arrowDown.layout(left, top, left + arrowDown.getMeasuredWidth(), top + arrowDown.getMeasuredHeight());

		left = right;
		top = t;
		bottom = top + list.getMeasuredHeight();
		right = left + list.getMeasuredHeight();
		list.layout(left, top, right, bottom);

	}

	@Override
	protected void onMeasure(
			int widthMeasureSpec,
			int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		measureChild(arrowDown, widthMeasureSpec, heightMeasureSpec);
		measureChild(arrowUp, widthMeasureSpec, heightMeasureSpec);

		int scrollerHeight = height - arrowDown.getMeasuredHeight() - arrowUp.getMeasuredHeight();
		measureChild(scroller, widthMeasureSpec, MeasureSpec.makeMeasureSpec(scrollerHeight, MeasureSpec.EXACTLY));

		int listWidth = width - scroller.getMeasuredWidth();
		measureChild(list, MeasureSpec.makeMeasureSpec(listWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

		setMeasuredDimension(width, height);
	}
}
