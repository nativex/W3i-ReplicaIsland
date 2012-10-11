package com.w3i.torch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.w3i.torch.R;

public class OptionsSlider extends Option {
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
		View seekerLayout = LayoutInflater.from(getContext()).inflate(R.layout.ui_layout_sliderbox, null);
		ViewGroup options = this;
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics());
		setPadding(padding, padding, padding, padding);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, R.id.ui_option_description);
		seekerLayout.setLayoutParams(params);
		options.addView(seekerLayout);
		seeker = (SeekBar) seekerLayout.findViewById(R.id.ui_sliderbox_slider);
		seeker.setMinimumWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250f, getResources().getDisplayMetrics()));
	}

	private void setAttribures(
			Context context,
			AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OptionsSlider);
		int pos = a.getInt(R.styleable.OptionsSlider_set_pos, 0);
		int max = a.getInt(R.styleable.OptionsSlider_set_max, 100);

		String minText = a.getString(R.styleable.OptionsSlider_set_min_text);
		String maxText = a.getString(R.styleable.OptionsSlider_set_max_text);

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
