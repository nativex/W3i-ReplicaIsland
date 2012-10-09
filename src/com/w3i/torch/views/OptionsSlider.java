package com.w3i.torch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.w3i.torch.R;

public class OptionsSlider extends RelativeLayout {
	private SeekBar seeker;

	public OptionsSlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		setAttribures(context, attrs);
	}

	public OptionsSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		setAttribures(context, attrs);
	}

	public OptionsSlider(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.ui_layout_sliderbox, this);
		seeker = (SeekBar) findViewById(R.id.ui_sliderbox_slider);
	}

	private void setAttribures(
			Context context,
			AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Options_OptionsSlider);
		String title = a.getString(R.styleable.Options_set_title);
		String description = a.getString(R.styleable.Options_OptionsSlider_set_description);
		int pos = a.getInt(R.styleable.Options_OptionsSlider_set_pos, 0);
		int max = a.getInt(R.styleable.Options_OptionsSlider_set_max, 100);

		String minText = a.getString(R.styleable.Options_OptionsSlider_set_min_text);
		String maxText = a.getString(R.styleable.Options_OptionsSlider_set_max_text);

		if (title != null) {
			TextView titleView = (TextView) findViewById(R.id.ui_sliderbox_title);
			titleView.setText(title);
		}

		if (description != null) {
			TextView descriptionView = (TextView) findViewById(R.id.ui_sliderbox_description);
			descriptionView.setText(description);
		}

		if (minText != null) {
			TextView minView = (TextView) findViewById(R.id.ui_sliderbox_min);
			minView.setText(minText);
		}

		if (maxText != null) {
			TextView maxView = (TextView) findViewById(R.id.ui_sliderbox_max);
			maxView.setText(maxText);
		}

		seeker.setMax(max);
		seeker.setProgress(pos);
	}

}
