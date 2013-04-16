package com.recharge.torch.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nativex.common.Log;
import com.recharge.torch.R;

public class CustomFontTextView extends TextView {
	private boolean addSpacing = false;

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

	@Override
	protected void onDraw(
			Canvas canvas) {

		if (addSpacing) {
			CharSequence text = getText();
			if ((text.length() > 1) && (text.charAt(1) != '\u00A0')) {
				setText(setSpacing(text));
			}
		}
		super.onDraw(canvas);
	}

	private CharSequence setSpacing(
			CharSequence text) {
		if (text.length() > 0) {
			StringBuilder s = new StringBuilder();
			s.append(text.charAt(0));
			for (int i = 1; i < text.length(); i++) {
				s.append("\u00A0" + text.charAt(i));
			}
			text = s.toString();
		}
		return text;
	}

	private void readAttrs(
			Context ctx,
			AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
		String customFont = a.getString(R.styleable.CustomFontTextView_setFont);
		addSpacing = a.getBoolean(R.styleable.CustomFontTextView_addSpacing, false);
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + customFont);
			setTypeface(tf);
			invalidate();
		} catch (Exception e) {
			Log.e("Could not get typeface: " + e.getMessage());
		}
		a.recycle();
	}

}
