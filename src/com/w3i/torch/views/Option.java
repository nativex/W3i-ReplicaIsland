package com.w3i.torch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.w3i.torch.R;

public class Option extends RelativeLayout {
	private String title;
	private String description;
	private TextView titleView;
	private TextView descriptionView;
	private Integer titleColor = null;
	private Integer descriptionColor = null;
	private boolean enabled = true;
	public static final int DISABLE_TEXT_COLOR = Color.argb(100, 255, 255, 255);

	public Option(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		setAttributes(context, attrs);
	}

	public Option(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		setAttributes(context, attrs);
	}

	public Option(Context context) {
		super(context);
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.ui_layout_option, this);
		titleView = (TextView) findViewById(R.id.ui_option_title);
		descriptionView = (TextView) findViewById(R.id.ui_option_description);
		// // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, getResources().getDisplayMetrics()),
		// // RelativeLayout.LayoutParams.WRAP_CONTENT);
		// RelativeLayout.LayoutParams params = (LayoutParams) titleView.getLayoutParams();
		// params.addRule(ALIGN_PARENT_TOP);
		// params.addRule(ALIGN_PARENT_LEFT);
		// titleView.setLayoutParams(params);
		//
		// // params = new RelativeLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, getResources().getDisplayMetrics()),
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// params = (LayoutParams) descriptionView.getLayoutParams();
		// params.addRule(BELOW, R.id.ui_option_title);
		// params.addRule(ALIGN_PARENT_LEFT);
		// descriptionView.setLayoutParams(params);

	}

	private void setAttributes(
			Context context,
			AttributeSet attr) {
		TypedArray a = context.obtainStyledAttributes(attr, R.styleable.Options);
		title = a.getString(R.styleable.Options_set_title);
		description = a.getString(R.styleable.Options_set_description);

		titleView.setText(title);
		descriptionView.setText(description);

		if (a.hasValue(R.styleable.Options_set_description_color)) {
			descriptionColor = a.getInteger(R.styleable.Options_set_description_color, Color.WHITE);
			descriptionView.setTextColor(descriptionColor);
		} else {
			descriptionColor = descriptionView.getCurrentTextColor();
		}
		if (a.hasValue(R.styleable.Options_set_title_color)) {
			titleColor = a.getInteger(R.styleable.Options_set_title_color, Color.WHITE);
			titleView.setTextColor(titleColor);
		} else {
			titleColor = titleView.getCurrentTextColor();
		}
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setTitle(
			String title) {
		this.title = title;
		titleView.setText(title);
	}

	public void setDescription(
			String description) {
		this.description = description;
		descriptionView.setText(description);
	}

	public void setDescriptionTextColor(
			Integer color) {
		if (color != null) {
			descriptionColor = color;
			if (enabled) {
				descriptionView.setTextColor(color);
			}
		}
	}

	public void setTitleTextColor(
			Integer color) {
		if (color != null) {
			titleColor = color;
			if (enabled) {
				titleView.setTextColor(color);
			}
		}
	}

	public void disable() {
		descriptionView.setTextColor(DISABLE_TEXT_COLOR);
		titleView.setTextColor(DISABLE_TEXT_COLOR);
		enabled = false;
	}

	public void enable() {
		descriptionView.setTextColor(descriptionColor);
		titleView.setTextColor(titleColor);
		enabled = true;
	}

}
