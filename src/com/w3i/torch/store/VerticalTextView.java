package com.w3i.torch.store;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VerticalTextView extends View {
	private String text;
	private Paint textPaint;
	private int textHeight = 0;
	private int textWidth = 0;
	private ORIENTATION orientation = ORIENTATION.LEFT;

	private static final float DEFAULT_TEXT_SIZE = 20;
	private static final int DEFAULT_TEXT_COLOR = Color.GRAY;

	public enum ORIENTATION {
		LEFT,
		RIGHT
	}

	public VerticalTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VerticalTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		textPaint = new Paint();
		textPaint.setColor(DEFAULT_TEXT_COLOR);
		textPaint.setTextSize(DEFAULT_TEXT_SIZE);
		textWidth = 0;
		textHeight = 0;
	}

	private void remeasureText() {
		if (text == null) {
			textWidth = 0;
			textHeight = 0;
		}
		textWidth = (int) (textPaint.measureText(text) + 0.5f);
		textHeight = (int) (textPaint.descent() - textPaint.ascent() + 0.5f);
	}

	public void setTextSize(
			float size) {
		textPaint.setTextSize(size);
		remeasureText();
		invalidate();
	}

	public void setText(
			String text) {
		this.text = text;
		remeasureText();
		invalidate();
	}

	public String getText() {
		return text;
	}

	public void setTextColor(
			int color) {
		textPaint.setColor(color);
		invalidate();
	}

	public void setOrientation(
			ORIENTATION orientation) {
		this.orientation = orientation;
		invalidate();
	}

	@Override
	protected void onMeasure(
			int widthMeasureSpec,
			int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(
			int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text

			result = textHeight + getPaddingLeft() + getPaddingRight();

			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(
			int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			result = textWidth + getPaddingTop() + getPaddingBottom();
			// Measure the text (beware: ascent is a negative number)

			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(
			Canvas canvas) {
		int height = textHeight + getPaddingBottom() + getPaddingTop();
		int width = textWidth + getPaddingLeft() + getPaddingRight();

		switch (orientation) {
		case LEFT:
			canvas.translate(textHeight, 0);
			canvas.rotate(90);
			break;
		case RIGHT:
		default:
			canvas.translate(0, textWidth);
			canvas.rotate(-90);
		}

		canvas.clipRect(0, 0, width, height, android.graphics.Region.Op.REPLACE);
		String textToDraw = text;
		if (text == null) {
			textToDraw = "";
		}
		canvas.drawText(textToDraw, getPaddingLeft(), getPaddingTop() - textPaint.ascent(), textPaint);
		super.onDraw(canvas);
	}

}
