package com.recharge.torch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.recharge.torch.R;
import com.recharge.torch.views.TorchCheckbox.State;

public class OptionsCheckbox extends Option {
	private String onText;
	private String offText;
	private String midText;
	private TorchCheckbox checkbox;
	private Integer onTextColor = null;
	private Integer offTextColor = null;
	private Integer midTextColor = null;

	private TorchCheckbox.OnCheckboxStateChanged checkboxListener;

	private View.OnClickListener onClick = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			switch (checkbox.getCheckState()) {
			case CHECKED:
				if (checkbox.isTristate()) {
					checkbox.setState(State.INDETERMINATE);
					setDescription(midText);
					setDescriptionTextColor(midTextColor);
				} else {
					checkbox.setState(State.UNCHECKED);
					setDescription(offText);
					setDescriptionTextColor(offTextColor);
				}
				break;
			case INDETERMINATE:
				checkbox.setState(State.UNCHECKED);
				setDescription(offText);
				setDescriptionTextColor(offTextColor);
				break;
			case UNCHECKED:
				checkbox.setState(State.CHECKED);
				setDescription(onText);
				setDescriptionTextColor(onTextColor);
				break;
			}

			if (checkboxListener != null) {
				checkboxListener.checkboxStateChanged(OptionsCheckbox.this, checkbox.getCheckState());
			}
		}
	};

	public OptionsCheckbox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		setAttributes(context, attrs);
	}

	public OptionsCheckbox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		setAttributes(context, attrs);
	}

	public OptionsCheckbox(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.ui_layout_checkbox, this);
		checkbox = (TorchCheckbox) findViewById(R.id.ui_checkbox_check);

		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, getResources().getDisplayMetrics());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
		params.addRule(CENTER_VERTICAL);
		params.addRule(ALIGN_PARENT_RIGHT);
		checkbox.setLayoutParams(params);
		checkbox.setOnClickListener(onClick);
		setOnClickListener(onClick);

		View titleView = findViewById(R.id.ui_option_title);
		View descriptionView = findViewById(R.id.ui_option_description);
		params = (LayoutParams) titleView.getLayoutParams();
		params.addRule(LEFT_OF, R.id.ui_checkbox_check);
		params = (LayoutParams) descriptionView.getLayoutParams();
		params.addRule(LEFT_OF, R.id.ui_checkbox_check);
	}

	private void setAttributes(
			Context context,
			AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.OptionsCheckbox);
		onText = attributes.getString(R.styleable.OptionsCheckbox_set_on_text);
		offText = attributes.getString(R.styleable.OptionsCheckbox_set_off_text);
		midText = attributes.getString(R.styleable.OptionsCheckbox_set_mid_text);
		if (attributes.hasValue(R.styleable.OptionsCheckbox_set_on_text_color)) {
			onTextColor = attributes.getInteger(R.styleable.OptionsCheckbox_set_on_text_color, Color.WHITE);
		}
		if (attributes.hasValue(R.styleable.OptionsCheckbox_set_off_text_color)) {
			offTextColor = attributes.getInteger(R.styleable.OptionsCheckbox_set_off_text_color, Color.WHITE);
			setDescriptionTextColor(offTextColor);
		}
		if (attributes.hasValue(R.styleable.OptionsCheckbox_set_mid_text_color)) {
			midTextColor = attributes.getInteger(R.styleable.OptionsCheckbox_set_mid_text_color, Color.WHITE);
		}
		checkbox.setTristate(attributes.getBoolean(R.styleable.OptionsCheckbox_set_tristate, false));
		checkbox.setState(State.UNCHECKED);
		setDescription(offText);
	}

	public TorchCheckbox getCheckbox() {
		return checkbox;
	}

	public void disable() {
		super.disable();
		checkbox.setOnClickListener(null);
		setOnClickListener(null);
		checkbox.disable();
	}

	public void enable() {
		super.enable();
		checkbox.setOnClickListener(onClick);
		setOnClickListener(onClick);
		checkbox.enable();
	}

	public void setOnCheckboxStateListener(
			TorchCheckbox.OnCheckboxStateChanged listener) {
		checkboxListener = listener;
	}

}
