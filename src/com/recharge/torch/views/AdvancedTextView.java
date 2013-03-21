package com.recharge.torch.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class AdvancedTextView extends View {
	private ArrayList<Text> content;
	private int textGap = DEFAULT_TEXT_HORIZONTAL_GAP;

	private static final int DEFAULT_TEXT_HORIZONTAL_GAP = 3;
	private static final int DEFAULT_TEXT_SIZE = 14;
	private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
	private static final int DEFAULT_TEXT_VERTICAL_GAP = 3;

	public AdvancedTextView(Context context) {
		super(context);
		init();
	}

	public AdvancedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AdvancedTextView(Context context, AttributeSet attrs, int theme) {
		super(context, attrs, theme);
		init();
	}

	private void init() {
		content = new ArrayList<Text>();
		// setBackgroundColor(Color.RED);
	}

	public void addText(
			Text text) {
		content.add(text);
		text.isAddedInView = true;
		requestLayout();
		invalidate();
	}

	public void setText(
			Text... text) {
		content.clear();
		if (text != null) {
			for (int i = 0; i < text.length; i++) {
				content.add(text[i]);
				text[i].isAddedInView = true;
			}
		}
		requestLayout();
		invalidate();
	}

	public void setTextGapSize(
			int size) {
		textGap = size;
	}

	/**
	 * @see android.view.View#measure(int, int)
	 */
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
			for (Text t : content) {
				result += t.width;
			}
			result += (content.size() - 1) * textGap + getPaddingLeft() + getPaddingRight();

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
			for (Text t : content) {
				result = Math.max(result, (int) (t.height));
			}
			result += getPaddingTop() + getPaddingBottom();
			// Measure the text (beware: ascent is a negative number)

			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Render the text
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(
			Canvas canvas) {
		super.onDraw(canvas);

		float left = getPaddingLeft();
		float top = getPaddingTop();
		float bottom = getPaddingBottom();
		float right = 0;
		int count = content.size();

		for (int i = 0; i < count; i++) {
			Text t = content.get(i);
			t.draw(canvas, left, top, right, bottom);
			left += t.width + textGap;
			if (i == count) {
				right = getPaddingRight();
			}
		}
	}

	public class Text {
		private String text;
		private int size = DEFAULT_TEXT_SIZE;
		private int color = DEFAULT_TEXT_COLOR;
		private Paint textPaint;
		private boolean isAddedInView = false;
		private int width = 0;
		private int height = 0;
		private int gravity = Gravity.LEFT;

		public Text() {
			init();
		}

		public Text(String text) {
			init();
			setText(text);
		}

		private void init() {
			textPaint = new Paint();
			textPaint = new Paint();
			textPaint.setAntiAlias(true);
			// Must manually scale the desired text size to match screen density
			resetTextPaint();
			setPadding(3, 3, 3, 3);
		}

		private void resetTextPaint() {
			textPaint.setTextSize(size * getResources().getDisplayMetrics().density);
			textPaint.setColor(color);

			width = 0;
			height = 0;
			if (text != null) {
				String[] lines = text.split("\n");
				width = 0;
				if ((lines != null) && (lines.length > 0)) {
					for (String s : lines) {
						width = Math.max((int) textPaint.measureText(s), width);
					}
					height = lines.length * (DEFAULT_TEXT_VERTICAL_GAP + (int) (textPaint.descent() - textPaint.ascent())) - DEFAULT_TEXT_VERTICAL_GAP;
				}

			}

		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public void setText(
				String text) {
			this.text = text;
			resetTextPaint();
			if (isAddedInView) {
				requestLayout();
				invalidate();
			}
		}

		/**
		 * @return the size
		 */
		public int getSize() {
			return size;
		}

		/**
		 * @param size
		 *            the size to set
		 */
		public void setSize(
				int size) {
			this.size = size;
			resetTextPaint();
			if (isAddedInView) {
				requestLayout();
			}
			invalidate();
		}

		/**
		 * @return the color
		 */
		public int getColor() {
			return color;
		}

		/**
		 * @param color
		 *            the color to set
		 */
		public void setColor(
				int color) {
			this.color = color;
			resetTextPaint();
			if (isAddedInView) {
				invalidate();
			}
		}

		public void setGravity(
				int gravity) {
			this.gravity = gravity;
		}

		protected void draw(
				Canvas canvas,
				float left,
				float top,
				float right,
				float bottom) {
			if (text != null) {
				String[] lines = text.split("\n");
				if (lines != null) {
					float x;
					float y;
					if (lines.length == 1) {
						x = getLeftDrawPosition(text, left, right);
						y = getTopDrawPosition(top, bottom);
						canvas.drawText(text, x, y, textPaint);
					} else if (lines.length > 1) {
						y = getTopDrawPosition(top, bottom);
						for (int i = 0; i < lines.length; i++) {
							x = getLeftDrawPosition(lines[i], left, right);
							canvas.drawText(lines[i], x, y, textPaint);
							y += textPaint.descent() - textPaint.ascent();
						}
					}
				}
			}
		}

		private float getLeftDrawPosition(
				String text,
				float left,
				float right) {
			int gravity = this.gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
			switch (gravity) {
			case Gravity.RIGHT:
				return width - (right + textPaint.measureText(text));
			case Gravity.CENTER:
			case Gravity.CENTER_HORIZONTAL:
				return left + (width - textPaint.measureText(text)) / 2;
			case Gravity.LEFT:
			default:
				return left;
			}
		}

		private float getTopDrawPosition(
				float top,
				float bottom) {
			int gravity = this.gravity & Gravity.VERTICAL_GRAVITY_MASK;
			switch (gravity) {
			case Gravity.CENTER:
			case Gravity.CENTER_VERTICAL:
				float rectHeight = getMeasuredHeight();
				float baselineDescend = -((height / 2) + textPaint.ascent());
				float center = (rectHeight + top - bottom) / 2;
				return center + baselineDescend;
			case Gravity.BOTTOM:
				return getMeasuredHeight() - (bottom + textPaint.descent());
			case Gravity.TOP:
			default:
				return top - textPaint.ascent();
			}
		}
	}

}