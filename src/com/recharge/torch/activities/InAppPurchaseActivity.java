package com.recharge.torch.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.recharge.torch.gamesplatform.TorchInAppPurchaseManager;
import com.w3i.gamesplatformsdk.rest.entities.Category;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.offerwall.custom.views.CustomImageView;

public class InAppPurchaseActivity extends Activity {
	private ViewGroup container;
	private static final int ITEM_ICON_SIZE = 70;
	public static final int ID_ITEM_ICON = 3242;
	public static final int ID_ITEM_NAME = 2423;
	public static final int ID_ITEM_DESCRIPTION = 24234;
	public static final int ID_ITEM_PRICE = 112;

	private static int itemIconSize;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView scroller = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		itemIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ITEM_ICON_SIZE, getResources().getDisplayMetrics());
		container = layout;
		createCategories();
		scroller.addView(container);
		addContentView(scroller, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
	}

	private void createCategories() {
		List<Category> categories = TorchInAppPurchaseManager.getCategories();
		for (Category cat : categories) {
			CategoryView view = new CategoryView(this);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
			container.addView(view);
			view.setName(cat.getDisplayName());
			addItemsInCategory(cat, view);
		}
	}

	private void addItemsInCategory(
			Category cat,
			CategoryView categoryContainer) {
		for (Item item : cat.getItems()) {
			CategoryItem view = new CategoryItem(this);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
			view.setIcon(item.getStoreImageUrl());
			view.setName(item.getDisplayName());
			view.setDescription(item.getDescription());
			view.setPrice("$" + item.getPurchasePrice().toString());
			view.setItem(item);
			view.setOnClickListener(onItemClick);
			categoryContainer.addView(view);
		}
	}

	private View.OnClickListener onItemClick = new OnClickListener() {

		@Override
		public void onClick(
				View v) {
			if (v != null) {
				Item item = ((CategoryItem) v).getItem();
				if (item != null) {
					TorchInAppPurchaseManager.buyItem(InAppPurchaseActivity.this, item);
				}

			}
		}
	};

	private class CategoryView extends LinearLayout {
		private TextView label;

		public CategoryView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public CategoryView(Context context) {
			super(context);
			init();
		}

		private void init() {
			label = new TextView(getContext());

			setOrientation(LinearLayout.VERTICAL);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			label.setLayoutParams(params);

			label.setMinHeight(80);
			label.setPadding(5, 5, 5, 5);
			label.setTextSize(16f);
			label.setTextColor(Color.WHITE);

			addView(label);
		}

		public void setName(
				String name) {
			label.setText(name);
		}

	}

	private class CategoryItem extends RelativeLayout {
		private CustomImageView icon;
		private TextView name;
		private TextView description;
		private TextView price;
		private Item item;

		public CategoryItem(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public CategoryItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public CategoryItem(Context context) {
			super(context);
			init();
		}

		private void init() {
			icon = new CustomImageView(getContext());
			name = new TextView(getContext());
			description = new TextView(getContext());
			price = new TextView(getContext());

			icon.setId(ID_ITEM_ICON);
			name.setId(ID_ITEM_NAME);
			description.setId(ID_ITEM_DESCRIPTION);
			price.setId(ID_ITEM_PRICE);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(itemIconSize, itemIconSize);
			params.addRule(ALIGN_PARENT_LEFT);
			params.addRule(CENTER_VERTICAL);
			icon.setLayoutParams(params);

			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(ALIGN_PARENT_TOP);
			params.addRule(RIGHT_OF, ID_ITEM_ICON);
			name.setLayoutParams(params);

			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(BELOW, ID_ITEM_NAME);
			params.addRule(RIGHT_OF, ID_ITEM_ICON);
			description.setLayoutParams(params);

			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(BELOW, ID_ITEM_DESCRIPTION);
			params.addRule(ALIGN_PARENT_RIGHT);
			price.setLayoutParams(params);

			name.setTextColor(Color.LTGRAY);
			name.setTextSize(14f);

			description.setTextColor(Color.WHITE);
			description.setTextSize(11f);

			price.setTextColor(Color.YELLOW);
			price.setTextSize(13f);

			addView(icon);
			addView(name);
			addView(description);
			addView(price);
		}

		public void setIcon(
				String url) {
			icon.setImageFromInternet(url);
		}

		public void setName(
				String name) {
			this.name.setText(name);
		}

		public void setDescription(
				String text) {
			description.setText(text);
		}

		public void setPrice(
				String price) {
			this.price.setText(price);
		}

		public void setItem(
				Item item) {
			this.item = item;
		}

		public Item getItem() {
			return item;
		}

	}

}
