package com.w3i.replica.replicaisland.activities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.skins.Skin;
import com.w3i.replica.replicaisland.skins.SkinManager;
import com.w3i.replica.replicaisland.skins.SkinManager.Skins;

public class SkinSelectionActivity extends Activity {
	private LinearLayout skinList;

	private View.OnClickListener onSkinClicked = new View.OnClickListener() {

		public void onClick(
				View v) {
			Object tag = v.getTag();
			if (tag instanceof Skins) {
				Skins skin = (Skins) tag;
				SkinManager.setSelectedSkin(skin);
				finish();
			}
		}
	};

	protected void onCreate(
			android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skin_activity);

		skinList = (LinearLayout) findViewById(R.id.skinActivityList);

		for (Skins s : Skins.values()) {
			addSkin(s);
		}
	}

	private void addSkin(
			Skins skin) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup skinItem = (ViewGroup) inflater.inflate(R.layout.skin_list_item, null);
		ImageView skinImage = (ImageView) skinItem.findViewById(R.id.skinItemImage);
		TextView skinName = (TextView) skinItem.findViewById(R.id.skinItemName);
		TextView skinDescription = (TextView) skinItem.findViewById(R.id.skinItemDescription);

		Skin s = SkinManager.getSkin(skin);
		skinImage.setImageResource(s.getImage());
		skinName.setText(s.getName());
		skinDescription.setText(s.getDescription());
		skinItem.setOnClickListener(onSkinClicked);
		skinItem.setTag(skin);
		skinList.addView(skinItem);
	}
}
