package com.w3i.replica.replicaisland.store;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.w3i.offerwall.custom.views.CustomImageView;
import com.w3i.offerwall.custom.views.ScrollingTextView;
import com.w3i.replica.replicaisland.R;

public class ReplicaInfoDialog extends Dialog {

	private View.OnClickListener onCloseClicked = new View.OnClickListener() {

		@Override
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
		View button = findViewById(R.id.infoDialogButton);
		button.setOnClickListener(onCloseClicked);
		getWindow().setBackgroundDrawableResource(R.drawable.info_dialog_background);
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

	public void setButtonText(
			String text) {
		Button button = (Button) findViewById(R.id.infoDialogButton);
		button.setText(text);
	}

	public void setButtonListener(
			View.OnClickListener listener) {
		View button = findViewById(R.id.infoDialogButton);
		button.setOnClickListener(listener);
	}

}
