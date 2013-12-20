package com.recharge.torch.views;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nativex.monetization.custom.views.CustomImageView;
import com.nativex.monetization.manager.DensityManager;
import com.recharge.torch.R;
import com.recharge.torch.views.InAppPurchaseCategoryScroller.OnCategoryChanged;

public class InAppPurchaseList extends ViewGroup {
	private InAppPurchaseCategoryScroller scroller;
	private InAppPurchaseItemsList list;
	private ImageView arrowUp;
	private ImageView arrowDown;
	private TextView title;
	private List<Category> categories;

	public static final int CATEGORY_PADDING = 5;
	public static final int LIST_PADDING = 15;
	private static final int ARROW_PADDING = 10;

	private int listPadding = 0;

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

	private View.OnClickListener onArrowClick = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			if (v == arrowUp) {
				scroller.scrollToCategory(-1);
			} else if (v == arrowDown) {
				scroller.scrollToCategory(1);
			}
		}
	};

	private void init() {
		scroller = new InAppPurchaseCategoryScroller(getContext());
		list = new InAppPurchaseItemsList(getContext());
		arrowUp = new ImageView(getContext());
		arrowDown = new ImageView(getContext());
		title = new TextView(getContext());
		setWillNotDraw(false);

		listPadding = DensityManager.getDIP(getContext(), LIST_PADDING);

		list.setPadding(listPadding, listPadding, listPadding, listPadding);
		arrowUp.setPadding(ARROW_PADDING, ARROW_PADDING, ARROW_PADDING, ARROW_PADDING);
		arrowDown.setPadding(ARROW_PADDING, ARROW_PADDING, ARROW_PADDING, ARROW_PADDING);

		arrowUp.setImageResource(R.drawable.ui_iap_arrow_up);
		arrowDown.setImageResource(R.drawable.ui_iap_arrow_down);

		arrowUp.setOnClickListener(onArrowClick);
		arrowDown.setOnClickListener(onArrowClick);

		int titlePadding = DensityManager.getDIP(getContext(), 10);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20f);
		title.setGravity(Gravity.CENTER);
		title.setText("IAP Screen");
		title.setPadding(titlePadding, titlePadding, titlePadding, titlePadding);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		title.setLayoutParams(params);

		addView(title);
		addView(arrowUp);
		addView(arrowDown);
		addView(scroller);
		addView(list);
	}

	public void setOnItemClickListener(
			View.OnClickListener listener) {
		list.setOnItemClickListener(listener);
	}

	private OnCategoryChanged categoryListener = new OnCategoryChanged() {

		@Override
		public void onChanged(
				int index,
				int oldIndex) {
			if (categories != null) {
				if ((categories.size() > index) && (index >= 0)) {
					Category active = categories.get(index);
					list.loadCategory(active);
					if (index > 0) {
						arrowUp.setVisibility(View.VISIBLE);
					} else {
						arrowUp.setVisibility(View.GONE);
					}
					if (index < categories.size() - 1) {
						arrowDown.setVisibility(View.VISIBLE);
					} else {
						arrowDown.setVisibility(View.GONE);
					}
					return;
				}
			}
			arrowUp.setVisibility(View.GONE);
			arrowDown.setVisibility(View.GONE);
		}
	};

	public void loadCategories(
			List<Category> categories) {
		if (this.categories != categories) {
			this.categories = categories;
			scroller.reset();
			list.reset();
			scroller.setOnCategoryChangedListener(categoryListener);
			int categoryPadding = DensityManager.getDIP(getContext(), CATEGORY_PADDING);
			if ((categories != null) && (categories.size() > 0)) {
				for (Category category : categories) {
					CustomImageView image = new CustomImageView(getContext());
					ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(80, 80);
					image.setLayoutParams(params);
					image.setImageFromInternet(category.getStoreImageUrl());
					image.setBackgroundResource(R.drawable.ui_iap_activity_item_container);
					image.setPadding(categoryPadding, categoryPadding, categoryPadding, categoryPadding);
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
		int scrollerWidth = Math.max(Math.max(scroller.getMeasuredWidth(), arrowUp.getMeasuredWidth()), arrowDown.getMeasuredWidth());
		int left, right, bottom, top, scrollerTop, scrollerBottom, titleBottom;

		left = l;
		top = t;
		bottom = t + title.getMeasuredHeight();
		right = r;
		titleBottom = bottom;
		title.layout(left, top, right, bottom);

		left = l + ((scrollerWidth - arrowUp.getMeasuredWidth()) / 2);
		top = titleBottom;
		bottom = top + arrowUp.getMeasuredHeight();
		right = left + arrowUp.getMeasuredWidth();
		scrollerTop = bottom;
		arrowUp.layout(left, top, right, bottom);

		left = l + ((scrollerWidth - arrowDown.getMeasuredWidth()) / 2);
		bottom = b;
		right = left + arrowDown.getMeasuredWidth();
		top = bottom - arrowDown.getMeasuredHeight();
		scrollerBottom = top;
		arrowDown.layout(left, top, right, bottom);

		top = scrollerTop;
		left = l + ((scrollerWidth - scroller.getMeasuredWidth()) / 2);
		right = left + scroller.getMeasuredWidth();
		bottom = scrollerBottom;
		scroller.layout(left, top, right, bottom);

		left = scrollerWidth - listPadding;
		top = titleBottom;
		bottom = b;
		right = r;
		list.layout(left, top, right, bottom);
	}

	private int oldWidthMeasureSpec, oldHeightMeasureSpec;

	@Override
	protected void onMeasure(
			int widthMeasureSpec,
			int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
		if ((widthMeasureSpec != oldWidthMeasureSpec) || (heightMeasureSpec != oldHeightMeasureSpec)) {
			oldHeightMeasureSpec = heightMeasureSpec;
			oldWidthMeasureSpec = widthMeasureSpec;

			measureChild(arrowDown, widthMeasureSpec, heightMeasureSpec);
			measureChild(arrowUp, widthMeasureSpec, heightMeasureSpec);
			measureChild(scroller, widthMeasureSpec, heightMeasureSpec);
			measureChild(title, widthMeasureSpec, heightMeasureSpec);
		}
		measureChild(list, widthMeasureSpec, heightMeasureSpec);
	}
}
