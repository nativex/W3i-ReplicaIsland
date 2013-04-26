package com.recharge.torch.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.recharge.torch.R;

public class InAppPurchaseCategoryScroller extends ViewGroup {
	private Scroller scroller;
	private int mTouchSlop;
	private float lastMotionY;
	private boolean scrolling = false;
	private LinearLayout container;
	private int activeChildPosition = 0;
	private int activeChildIndex = 0;

	private OnCategoryChanged onCategoryChangedListener = null;

	public InAppPurchaseCategoryScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InAppPurchaseCategoryScroller(Context context) {
		super(context);
		init();
	}

	private void init() {
		scroller = new Scroller(getContext());
		container = new LinearLayout(getContext());
		container.setOrientation(LinearLayout.VERTICAL);
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
		addView(container);
	}

	@Override
	public boolean onInterceptTouchEvent(
			MotionEvent ev) {
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (scrolling)) {
			return true;
		}

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_MOVE: {
			final float y = ev.getY();
			final int yDiff = (int) Math.abs(y - lastMotionY);
			if (yDiff > mTouchSlop) {
				scrolling = true;
				lastMotionY = y;
			}
			break;
		}

		case MotionEvent.ACTION_DOWN: {
			final float y = ev.getY();
			scrolling = false;
			lastMotionY = y;
			scrolling = !scroller.isFinished();
			scroller.abortAnimation();
			break;
		}

		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			scrolling = false;
			snapToActiveCategory();
			break;
		}

		return scrolling;
	}

	public void addItem(
			View item) {
		boolean changeCategory = false;
		if (container.getChildCount() == 0) {
			changeCategory = true;
		}
		container.addView(item);
		if (changeCategory) {
			changeCategory(0, true);
		}
	}

	@Override
	public boolean onTouchEvent(
			MotionEvent ev) {
		final int action = ev.getAction();

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			scrolling = getChildCount() != 0;
			if (!scrolling) {
				return false;
			}

			lastMotionY = ev.getY();
			break;
		}
		case MotionEvent.ACTION_MOVE:
			if (scrolling) {
				final float y = ev.getY();
				final int deltaY = (int) (lastMotionY - y);
				lastMotionY = y;

				scrollBy(0, deltaY);
				checkForCategoryChange();
				postInvalidate();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (scrolling) {
				snapToActiveCategory();
			}
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(
			Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		int y = getScrollY() + activeChildPosition;
		canvas.drawLine(0, y, getWidth(), y, paint);
	}

	public void scrollToCategory(
			int direction) {
		if (changeCategory(activeChildIndex + direction)) {
			snapToActiveCategory();
		}
	}

	private void checkForCategoryChange() {
		if (container.getChildCount() == 0) {
			return;
		}
		if (activeChildIndex >= container.getChildCount()) {
			activeChildIndex = container.getChildCount() - 1;
		} else if (activeChildIndex < 0) {
			activeChildIndex = 0;
		}

		int activeScrollPosition = getScrollY() + activeChildPosition;
		View currentChild = container.getChildAt(activeChildIndex);
		if ((activeScrollPosition >= currentChild.getTop()) && (activeScrollPosition <= currentChild.getBottom())) {
			// If the active category line is between the child's top and bottom then the current child is still the active one.
			// If the scrolling is not insanely fast most of the cases when this method is called will hit here.
			return;
		} else if (activeScrollPosition > currentChild.getBottom()) {
			// The active line is below this child's bottom.
			// find the next active child.
			for (int i = activeChildIndex + 1; i < container.getChildCount(); i++) {
				// find the next child which top is above the active line
				// If the scrolling is not extremely fast then the first child should be it
				View child = container.getChildAt(i);
				if (child.getTop() <= activeScrollPosition) {
					changeCategory(i);
					return;
				}
			}
		} else if (activeScrollPosition < currentChild.getTop()) {
			// The active line is above this child's top.
			// find the next active child.
			for (int i = activeChildIndex - 1; i >= 0; i--) {
				// find the next child which bottom is below the active line
				// If the scrolling is not extremely fast then the first child should be it
				View child = container.getChildAt(i);
				if (child.getBottom() >= activeScrollPosition) {
					changeCategory(i);
					return;
				}
			}
		}
	}

	private boolean changeCategory(
			int index) {
		return changeCategory(index, false);
	}

	private boolean changeCategory(
			int index,
			boolean forceChange) {
		if ((container.getChildCount() == 0) || (index < 0) || (index >= container.getChildCount())) {
			return false;
		}
		if ((!forceChange) && ((index == activeChildIndex))) {
			return false;
		}
		int oldIndex = activeChildIndex;
		if (oldIndex != index) {
			container.getChildAt(oldIndex).setBackgroundResource(R.drawable.ui_iap_activity_item_container);
		}
		container.getChildAt(index).setBackgroundResource(R.drawable.ui_iap_activity_item_container_selected);
		activeChildIndex = index;
		if (onCategoryChangedListener != null) {
			onCategoryChangedListener.onChanged(index, oldIndex);
		}
		return true;
	}

	private void snapToActiveCategory() {
		if ((container.getChildCount() == 0) || (activeChildIndex < 0) || (activeChildIndex >= container.getChildCount())) {
			return;
		}
		View child = container.getChildAt(activeChildIndex);
		smoothScrollTo(child.getTop() + (child.getHeight() / 2) - activeChildPosition);
	}

	private void smoothScrollTo(
			int y) {
		final int dy = y - getScrollY();
		scroller.abortAnimation();
		scroller.startScroll(0, getScrollY(), 0, dy, Math.max(750, Math.abs(dy)));
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			int oldY = getScrollY();
			int y = scroller.getCurrY();

			if (oldY != y) {
				scrollBy(0, y - oldY);
				onScrollChanged(0, getScrollY(), 0, oldY);
			}
			postInvalidate();
		}
	}

	private boolean firstLayout = true;

	public void reset() {
		container.removeAllViews();
	}

	@Override
	protected void onMeasure(
			int widthMeasureSpec,
			int heightMeasureSpec) {
		int width = 0, height = MeasureSpec.getSize(heightMeasureSpec);
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, MeasureSpec.AT_MOST));
			if (child.getMeasuredWidth() > width) {
				width = child.getMeasuredWidth();
			}
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(
			boolean changed,
			int l,
			int t,
			int r,
			int b) {
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
			activeChildPosition = (b - t) / 2;
			if (firstLayout) {
				snapToActiveCategory();
				firstLayout = false;
			}
		}
	}

	public void setOnCategoryChangedListener(
			OnCategoryChanged listener) {
		onCategoryChangedListener = listener;
	}

	public interface OnCategoryChanged {
		public void onChanged(
				int newIndex,
				int oldIndex);
	}

}
