package com.w3i.replica.replicaisland.skins;

import com.w3i.replica.replicaisland.AnimationComponent.PlayerAnimations;
import com.w3i.replica.replicaisland.R;

public class PirateCaptainSkin extends Skin {

	public PirateCaptainSkin() {
		super();
		init();
	}

	private void init() {
		// move
		int[] animationFrames = new int[10];
		animationFrames[0] = R.drawable.captain_walk_01;
		animationFrames[1] = R.drawable.captain_walk_02;
		animationFrames[2] = R.drawable.captain_walk_03;
		animationFrames[3] = R.drawable.captain_walk_04;
		animationFrames[4] = R.drawable.captain_walk_05;
		animationFrames[5] = R.drawable.captain_walk_06;
		animationFrames[6] = R.drawable.captain_walk_07;
		animationFrames[7] = R.drawable.captain_walk_08;
		animationFrames[8] = R.drawable.captain_walk_09;
		animationFrames[9] = R.drawable.captain_walk_10;
		setAnimationFrames(PlayerAnimations.MOVE, animationFrames);

		// stand
		setAnimationFrames(PlayerAnimations.IDLE, R.drawable.captain_stand_01);

		// move air
		setAnimationFrames(PlayerAnimations.MOVE_AIR, R.drawable.captain_stand_01);

		setTitleImage(R.drawable.ui_main_screen_character);
		setSkinType(SkinType.PLAYER);
		setImage(R.drawable.captain_stand_01);
		setName("Pirate Captain");
		setDescription("A pirate captain which ship crashed on the island. While wandering trying to find a way off this rock, he sensed The Source and is now looking for it");
	}
}
