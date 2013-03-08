package com.recharge.torch.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.recharge.torch.R;
import com.w3i.common.Log;

public class EULAActivity extends Activity {
	private TextView text;

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_eula);
		Log.enableLogging(true);
		text = (TextView) findViewById(R.id.uiEULAText);
		new AsyncReader();
	}

	private class AsyncReader implements Runnable {

		public AsyncReader() {
			// Thread thread = new Thread(this);
			// thread.start();
			run();
		}

		@Override
		public void run() {

			try {
				InputStream is = getAssets().open("EULA");
				final String eulaText = convertStreamToString(is);
				text.post(new Runnable() {

					@Override
					public void run() {
						text.setText(eulaText);
						text.setTextColor(Color.WHITE);
					}
				});
			} catch (Exception e) {
				Log.e("EULAActivity: Exception caught while reading content from file", e);
			}
		}

		public String convertStreamToString(
				InputStream is) {
			/*
			 * To convert the InputStream to String we use the Reader.read(char[] buffer) method. We iterate until the Reader return -1 which means there's no more data to read. We use the
			 * StringWriter class to produce the string.
			 */
			try {
				if (is != null) {
					Writer writer = new StringWriter();

					char[] buffer = new char[1024];
					try {
						Reader reader = new BufferedReader(new InputStreamReader(is, "Cp1252"));
						int n;
						while ((n = reader.read(buffer)) != -1) {
							writer.write(buffer, 0, n);
						}
					} finally {
						is.close();
					}
					return writer.toString();
				}
			} catch (Exception e) {
				Log.e("MAAPManager.convertStreamToString(): exception caught.", e);
			}
			return "";
		}

	}
}
