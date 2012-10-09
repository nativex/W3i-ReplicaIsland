package com.w3i.torch.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.w3i.torch.R;

public class TorchCheckbox extends ImageView {
	private State state;
	private boolean tristate;

	private int bgChecked = R.drawable.ui_checkbox_checked;
	private int bgUnchecked = R.drawable.ui_checkbox_unchecked;
	private int bgIndeterminate = R.drawable.ui_checkbox_indeterminate;
	private OnCheckboxStateChanged onStateChange;

	private View.OnClickListener onCheckboxClick = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			switch (state) {
			case CHECKED:
				if (isTristate()) {
					state = State.INDETERMINATE;
					setBackgroundResource(bgIndeterminate);
				} else {
					state = State.UNCHECKED;
					setBackgroundResource(bgUnchecked);
				}
				break;
			case INDETERMINATE:
				state = State.UNCHECKED;
				setBackgroundResource(bgUnchecked);
				break;
			case UNCHECKED:
				state = State.CHECKED;
				setBackgroundResource(bgChecked);
				break;
			}
			if (onStateChange != null) {
				onStateChange.checkboxStateChanged(state);
			}
		}
	};

	public enum State {
		CHECKED,
		UNCHECKED,
		INDETERMINATE;
	}

	public TorchCheckbox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TorchCheckbox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TorchCheckbox(Context context) {
		super(context);
		init();
	}

	private void init() {
		setTristate(false);
		state = State.UNCHECKED;
		setOnClickListener(onCheckboxClick);
		setBackgroundResource(bgUnchecked);
	}

	public void setOnStateChange(
			OnCheckboxStateChanged listener) {
		onStateChange = listener;
	}

	public boolean isTristate() {
		return tristate;
	}

	public void setTristate(
			boolean tristate) {
		this.tristate = tristate;
	}

	public State getCheckState() {
		return state;
	}

	public boolean isChecked() {
		return state == State.CHECKED;
	}

	public interface OnCheckboxStateChanged {
		public void checkboxStateChanged(
				State state);
	}

	public void setState(
			State state) {
		switch (state) {
		case CHECKED:
			setBackgroundResource(bgChecked);
			break;
		case INDETERMINATE:
			setBackgroundResource(bgIndeterminate);
			break;
		case UNCHECKED:
			setBackgroundResource(bgUnchecked);
			break;

		}
		this.state = state;
	}
}
