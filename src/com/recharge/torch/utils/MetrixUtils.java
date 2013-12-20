package com.recharge.torch.utils;

import android.app.Activity;
import android.util.DisplayMetrics;


public class MetrixUtils {

	public static float getDeviceScreenDiagInches(
			Activity activity) {
		final DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int orientation = activity.getWindowManager().getDefaultDisplay().getOrientation();

		float heightInches;
		float widthInches;

		if ((orientation == 0) || (orientation == 2)) {
			heightInches = metrics.heightPixels / metrics.ydpi;
			widthInches = metrics.widthPixels / metrics.xdpi;
		} else {
			heightInches = metrics.heightPixels / metrics.xdpi;
			widthInches = metrics.widthPixels / metrics.ydpi;
		}
		float diagonalInches = (float) Math.sqrt(heightInches * heightInches + widthInches * widthInches);
		diagonalInches = (float) Math.floor(diagonalInches * 10 + 0.5) / 10;

		return diagonalInches;
	}
}
