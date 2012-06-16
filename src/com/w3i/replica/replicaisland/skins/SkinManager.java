package com.w3i.replica.replicaisland.skins;

public class SkinManager {
	private static Skins selectedSkin = Skins.DEFAULT;

	public enum Skins {
		DEFAULT,
		PIRATE_CAPTAIN;
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
		}
		return null;
	}

	public static void setSelectedSkin(
			Skins skin) {
		selectedSkin = skin;
	}

}
