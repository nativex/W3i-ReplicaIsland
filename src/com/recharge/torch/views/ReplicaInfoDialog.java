package com.recharge.torch.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nativex.monetization.custom.views.CustomImageView;
import com.nativex.monetization.custom.views.ScrollingTextView;
import com.recharge.torch.R;

public class ReplicaInfoDialog extends Dialog {

	private View.OnClickListener onCloseClicked = new View.OnClickListener() {

		public void onClick(
				View arg0) {
			dismiss();
		}
	};

	public ReplicaInfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public ReplicaInfoDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public ReplicaInfoDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_dialog);
		View close = findViewById(R.id.infoDialogCloseButton);
		close.setOnClickListener(onCloseClicked);
		TextView button = (TextView) findViewById(R.id.infoDialogButton);
		button.setOnClickListener(onCloseClicked);
		getWindow().setBackgroundDrawableResource(R.drawable.info_dialog_background);
		button.setText("OK");
		button.setTextSize(20);

	}

	@Override
	public void setTitle(
			CharSequence title) {
		ScrollingTextView titleView = (ScrollingTextView) findViewById(R.id.infoDialogTitle);
		titleView.setText(title);
	}

	@Override
	public void setTitle(
			int titleId) {
		String title = getContext().getResources().getString(titleId);
		setTitle(title);
	}

	public void setErrorMessage(
			String text) {
		TextView errorView = (TextView) findViewById(R.id.infoDialogError);
		if (text == null) {
			errorView.setVisibility(View.GONE);
		} else {
			errorView.setVisibility(View.VISIBLE);
			errorView.setText(text);
		}
	}

	public void setIcon(
			String url) {
		CustomImageView icon = (CustomImageView) findViewById(R.id.infoDialogIcon);
		icon.setImageFromInternet(url);
	}

	public void setIcon(
			int resource) {
		CustomImageView icon = (CustomImageView) findViewById(R.id.infoDialogIcon);
		icon.setImageResource(resource);
	}

	public void setIcon(
			Drawable drawable) {
		CustomImageView icon = (CustomImageView) findViewById(R.id.infoDialogIcon);
		icon.setImageDrawable(drawable);
	}

	public void setDescripton(
			String text) {
		TextView description = (TextView) findViewById(R.id.infoDialogDescription);
		description.setText(text);
	}

	public void setDescripton(
			int resId) {
		setDescripton(getContext().getResources().getString(resId));
	}

	public void setButtonText(
			String text,
			float textSize) {
		TextView button = (TextView) findViewById(R.id.infoDialogButton);
		button.setText(text);
		if (textSize > 0) {
			button.setTextSize(textSize);
		}
	}

	public void setButtonText(
			int resId) {
		setButtonText(getContext().getResources().getString(resId));
	}

	public void setButtonText(
			int resId,
			float size) {
		setButtonText(getContext().getResources().getString(resId), size);
	}

	public void setButtonText(
			String text) {
		setButtonText(text, 0);
	}

	public void setButtonListener(
			View.OnClickListener listener) {
		View button = findViewById(R.id.infoDialogButton);
		button.setOnClickListener(listener);
	}

	public void hideIcon() {
		View icon = findViewById(R.id.infoDialogIcon);
		icon.setVisibility(View.GONE);
	}

	public void setCloseListener(
			View.OnClickListener listener) {
		View close = findViewById(R.id.infoDialogCloseButton);
		close.setOnClickListener(listener);
	}

}
