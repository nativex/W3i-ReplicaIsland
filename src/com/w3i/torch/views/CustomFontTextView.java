package com.w3i.torch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.w3i.common.Log;
import com.w3i.torch.R;

public class CustomFontTextView extends TextView {

	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		readAttrs(context, attrs);
	}

	public CustomFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		readAttrs(context, attrs);
	}

	public CustomFontTextView(Context context) {
		super(context);
	}

	private void readAttrs(
			Context ctx,
			AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
		String customFont = a.getString(R.styleable.CustomFontTextView_setFont);
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + customFont);
			setTypeface(tf);
		} catch (Exception e) {
			Log.e("Could not get typeface: " + e.getMessage());
		}
		a.recycle();
	}

}
