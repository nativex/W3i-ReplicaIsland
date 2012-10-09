package com.w3i.torch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.w3i.torch.R;
import com.w3i.torch.views.TorchCheckbox.State;

public class OptionsCheckbox extends RelativeLayout {
	private String onText;
	private String offText;
	private String midText;
	private TorchCheckbox checkbox;
	private TextView description;

	private TorchCheckbox.OnCheckboxStateChanged checkboxListener;

	private View.OnClickListener onClick = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			switch (checkbox.getCheckState()) {
			case CHECKED:
				if (checkbox.isTristate()) {
					checkbox.setState(State.INDETERMINATE);
					description.setText(midText);
				} else {
					checkbox.setState(State.UNCHECKED);
					description.setText(offText);
				}

				break;
			case INDETERMINATE:
				checkbox.setState(State.UNCHECKED);
				description.setText(offText);
				break;
			case UNCHECKED:
				checkbox.setState(State.CHECKED);
				description.setText(onText);
				break;
			}

			if (checkboxListener != null) {
				checkboxListener.checkboxStateChanged(checkbox.getCheckState());
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
		description = (TextView) findViewById(R.id.ui_checkbox_description);
		checkbox.setOnClickListener(null);
		setOnClickListener(onClick);
	}

	private void setAttributes(
			Context context,
			AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Options_Checkbox);
		String title = attributes.getString(R.styleable.Options_set_title);
		onText = attributes.getString(R.styleable.Options_Checkbox_set_on_text);
		offText = attributes.getString(R.styleable.Options_Checkbox_set_off_text);
		midText = attributes.getString(R.styleable.Options_Checkbox_set_mid_text);
		checkbox.setTristate(attributes.getBoolean(R.styleable.Options_Checkbox_set_tristate, false));
		if (title != null) {
			TextView titleView = (TextView) findViewById(R.id.ui_checkbox_title);
			titleView.setText(title);
		}

		if (offText != null) {
			description.setText(offText);
		}
	}

	public TorchCheckbox getCheckbox() {
		return checkbox;
	}

}
