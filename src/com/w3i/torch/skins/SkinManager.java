package com.w3i.torch.skins;

import android.R;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SkinManager {
	private static Skins selectedSkin = Skins.TORCH;
	private static boolean skinChanged = false;
	private static boolean mainScreenImageSet = false;

	public enum Skins {
		DEFAULT,
		PIRATE_CAPTAIN,
		TORCH;
	}

	private SkinManager() {
	}

	public static Skin getSelectedSkin() {
		return getSkin(selectedSkin);
	}

	public static Skin getSkin(
			Skins skin) {
		switch (skin) {
		case DEFAULT:
			return new DefaultPlayerSkin();
		case PIRATE_CAPTAIN:
			return new PirateCaptainSkin();
		case TORCH:
			return new TorchSkin();
		}
		return null;
	}

	public static void setSelectedSkin(
			Skins skin) {
		if (selectedSkin.ordinal() != skin.ordinal()) {
			skinChanged = true;
		}
		selectedSkin = skin;

	}

	public static boolean isSkinChanged() {
		return skinChanged;
	}

	public static void setSkinChanged(
			boolean skinChanged) {
		SkinManager.skinChanged = skinChanged;
	}

	public static void changeTitleScreenImage(
			final ImageView imageView) {
		if (imageView == null) {
			return;
		}
		if ((skinChanged) || (!mainScreenImageSet)) {
			if (!mainScreenImageSet) {
				imageView.setImageResource(getSkin(selectedSkin).getTitleImage());
			} else {
				AnimationSet set = new AnimationSet(true);
				Animation outAnim = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.fade_out);
				outAnim.setDuration(500);
				outAnim.setAnimationListener(new Animation.AnimationListener() {

					public void onAnimationStart(
							Animation animation) {
					}

					public void onAnimationRepeat(
							Animation animation) {

					}

					public void onAnimationEnd(
							Animation animation) {
						imageView.setImageResource(getSkin(selectedSkin).getTitleImage());
						Animation inAnim = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.fade_in);
						inAnim.setDuration(500);
						imageView.setAnimation(inAnim);
						inAnim.start();
					}
				});
				set.addAnimation(outAnim);

				imageView.setAnimation(set);
				set.start();
			}
			skinChanged = false;
			mainScreenImageSet = true;
		}

	}
}
