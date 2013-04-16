package com.recharge.torch.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nativex.monetization.custom.views.CustomImageView;
import com.nativex.monetization.custom.views.ScrollingTextView;
import com.nativex.monetization.dialogs.custom.AdvancedButton;
import com.nativex.monetization.manager.DensityManager;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class InAppPurchaseItem extends RelativeLayout {
	public static final int ICON_SIZE = 80;
	public static final int ICON_MARGIN = 7;

	public static final int ID_ICON = 12123;
	public static final int ID_TITLE = 1212344;
	public static final int ID_DESCRIPTION = 11232;
	public static final int ID_PRICE = 113442;
	public static final int ID_BUTTON = 1994;

	private CustomImageView icon;
	private TextView title;
	private TextView description;
	private TextView price;
	private Button getItButton;

	public InAppPurchaseItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public InAppPurchaseItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InAppPurchaseItem(Context context) {
		super(context);
		init();
	}

	private void init() {
		icon = new CustomImageView(getContext());
		title = new ScrollingTextView(getContext());
		description = new TextView(getContext());
		price = new TextView(getContext());
		getItButton = new AdvancedButton(getContext());

		icon.setId(ID_ICON);
		title.setId(ID_TITLE);
		description.setId(ID_DESCRIPTION);
		price.setId(ID_PRICE);
		getItButton.setId(ID_BUTTON);

		int iconSizeDIP = DensityManager.getDIP(getContext(), ICON_SIZE);
		int iconMargins = DensityManager.getDIP(getContext(), ICON_MARGIN);

		RelativeLayout.LayoutParams params = new LayoutParams(iconSizeDIP, iconSizeDIP);
		params.addRule(ALIGN_PARENT_LEFT);
		params.setMargins(iconMargins, iconMargins, iconMargins, iconMargins);
		icon.setLayoutParams(params);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(ALIGN_PARENT_TOP);
		params.addRule(RIGHT_OF, ID_ICON);
		params.addRule(ALIGN_PARENT_RIGHT);
		params.topMargin = iconMargins;
		params.bottomMargin = iconMargins;
		title.setLayoutParams(params);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, ID_TITLE);
		params.addRule(RIGHT_OF, ID_ICON);
		params.addRule(ALIGN_PARENT_RIGHT);
		description.setLayoutParams(params);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, ID_DESCRIPTION);
		params.addRule(RIGHT_OF, ID_ICON);
		params.addRule(ALIGN_PARENT_RIGHT);
		price.setLayoutParams(params);

		price.setGravity(Gravity.RIGHT);

		title.setTextColor(Color.WHITE);
		description.setTextColor(Color.WHITE);
		price.setTextColor(Color.WHITE);

		addView(icon);
		addView(title);
		addView(description);
		addView(price);
	}

	public void setIconUrl(
			String url) {
		icon.setImageFromInternet(url);
	}

	public void setTitle(
			String title) {
		this.title.setText(title);
	}

	public void setDescription(
			String text) {
		description.setText(text);
	}

	public void setPrice(
			String price) {
		this.price.setText("$" + price);
	}

	public void setItem(
			Item item) {
		icon.setImageFromInternet(item.getStoreImageUrl());
		title.setText(item.getDisplayName());
		description.setText(item.getDescription());
		price.setText(item.getPurchasePrice() + "");
	}

}
