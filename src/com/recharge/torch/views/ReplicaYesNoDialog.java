package com.recharge.torch.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nativex.monetization.custom.views.ScrollingTextView;
import com.recharge.torch.R;

public class ReplicaYesNoDialog extends Dialog {

	private View.OnClickListener onCloseClicked = new View.OnClickListener() {

		public void onClick(
				View arg0) {
			dismiss();
		}
	};

	public ReplicaYesNoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public ReplicaYesNoDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public ReplicaYesNoDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_dialog_yes_no);
		View close = findViewById(R.id.ui_dialog_yn_close);
		close.setOnClickListener(onCloseClicked);
		getWindow().setBackgroundDrawableResource(R.drawable.info_dialog_background);
		TextView negative = (TextView) findViewById(R.id.ui_dialog_yn_negative);
		negative.setOnClickListener(onCloseClicked);

	}

	@Override
	public void setTitle(
			CharSequence title) {
		ScrollingTextView titleView = (ScrollingTextView) findViewById(R.id.ui_dialog_yn_title);
		titleView.setText(title);
	}

	@Override
	public void setTitle(
			int titleId) {
		String title = getContext().getResources().getString(titleId);
		setTitle(title);
	}

	// public void setIcon(
	// String url) {
	// CustomImageView icon = (CustomImageView) findViewById(R.id.ui_dialog_yn_icon);
	// icon.setImageFromInternet(url);
	// }
	//
	// public void setIcon(
	// int resource) {
	// CustomImageView icon = (CustomImageView) findViewById(R.id.infoDialogIcon);
	// icon.setImageResource(resource);
	// }
	//
	// public void setIcon(
	// Drawable drawable) {
	// CustomImageView icon = (CustomImageView) findViewById(R.id.infoDialogIcon);
	// icon.setImageDrawable(drawable);
	// }

	public void setMessage(
			String text) {
		setMessage(text, 0);
	}

	public void setMessage(
			String text,
			float size) {
		TextView description = (TextView) findViewById(R.id.ui_dialog_yn_description);
		description.setText(text);
		if (size > 0) {
			description.setTextSize(size);
		}
	}

	public void setMessage(
			int resourceId,
			float size) {
		setMessage(getContext().getResources().getString(resourceId), size);
	}

	public void setMessage(
			int resourceId) {
		setMessage(resourceId, 0);
	}

	public void setPositiveButtonText(
			String text,
			float textSize) {
		TextView button = (TextView) findViewById(R.id.ui_dialog_yn_positive);
		button.setText(text);
		if (textSize > 0) {
			button.setTextSize(textSize);
		}
	}

	public void setPositiveButtonText(
			int resourceId,
			float textSize) {
		setPositiveButtonText(getContext().getResources().getString(resourceId));
	}

	public void setNegativeButtonText(
			int resourceId,
			float textSize) {
		setNegativeButtonText(getContext().getResources().getString(resourceId));
	}

	public void setNegativeButtonText(
			String text,
			float textSize) {
		TextView button = (TextView) findViewById(R.id.ui_dialog_yn_negative);
		button.setText(text);
		if (textSize > 0) {
			button.setTextSize(textSize);
		}
	}

	public void setPositiveButtonText(
			String text) {
		setPositiveButtonText(text, 0);
	}

	public void setPositiveButtonText(
			int resourceId) {
		setPositiveButtonText(resourceId, 0);
	}

	public void setNegativeButtonText(
			String text) {
		setNegativeButtonText(text, 0);
	}

	public void setNegativeButtonText(
			int resourceId) {
		setNegativeButtonText(resourceId, 0);
	}

	public void setPositiveButtonListener(
			View.OnClickListener listener) {
		View button = findViewById(R.id.ui_dialog_yn_positive);
		button.setOnClickListener(listener);
	}

	public void setNegativeButtonListener(
			View.OnClickListener listener) {
		View button = findViewById(R.id.ui_dialog_yn_negative);
		button.setOnClickListener(listener);
	}

	// public void hideIcon() {
	// View icon = findViewById(R.id.infoDialogIcon);
	// icon.setVisibility(View.GONE);
	// }

	public void setCloseListener(
			View.OnClickListener listener) {
		View close = findViewById(R.id.ui_dialog_yn_close);
		close.setOnClickListener(listener);
	}

}
