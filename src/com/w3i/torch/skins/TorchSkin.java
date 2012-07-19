package com.w3i.torch.skins;

import com.w3i.torch.AnimationComponent.PlayerAnimations;
import com.w3i.torch.R;

public class TorchSkin extends Skin {

	public TorchSkin() {
		super();

		setDeathAnimation();
		setMovementAnimations();
		setActionAnimations();

		setTitleImage(R.drawable.ui_main_menu_torch);
		setSkinType(SkinType.PLAYER);
		setImage(R.drawable.skin_player_torch_stand_01);
		setName("Torch");
		setDescription("A robot.");
	}

	private void setDeathAnimation() {
		// DEATH
		int[] deathAnimationFrames = new int[16];
		deathAnimationFrames[0] = R.drawable.skin_player_torch_die_01;
		deathAnimationFrames[1] = R.drawable.skin_player_torch_die_02;
		deathAnimationFrames[2] = R.drawable.skin_player_torch_die_01;
		deathAnimationFrames[3] = R.drawable.skin_player_torch_die_02;
		deathAnimationFrames[4] = R.drawable.skin_player_torch_explode_01;
		deathAnimationFrames[5] = R.drawable.skin_player_torch_explode_02;
		deathAnimationFrames[6] = R.drawable.skin_player_torch_explode_03;
		deathAnimationFrames[7] = R.drawable.skin_player_torch_explode_04;
		deathAnimationFrames[8] = R.drawable.skin_player_torch_explode_05;
		deathAnimationFrames[9] = R.drawable.skin_player_torch_explode_06;
		deathAnimationFrames[10] = R.drawable.skin_player_torch_explode_07;
		deathAnimationFrames[11] = R.drawable.skin_player_torch_explode_08;
		deathAnimationFrames[12] = R.drawable.skin_player_torch_explode_09;
		deathAnimationFrames[13] = R.drawable.skin_player_torch_explode_10;
		deathAnimationFrames[14] = R.drawable.skin_player_torch_explode_11;
		deathAnimationFrames[15] = R.drawable.skin_player_torch_explode_12;

		setAnimationFrames(PlayerAnimations.DEATH, deathAnimationFrames);
	}

	private void setMovementAnimations() {
		// IDLE (stand)
		setAnimationFrames(PlayerAnimations.IDLE, R.drawable.skin_player_torch_stand_01, R.drawable.skin_player_torch_stand_02);

		// MOVE
		setAnimationFrames(PlayerAnimations.MOVE, R.drawable.skin_player_torch_move_01, R.drawable.skin_player_torch_move_02);

		// MOVE FAST
		setAnimationFrames(PlayerAnimations.MOVE_FAST, R.drawable.skin_player_torch_move_fast_01, R.drawable.skin_player_torch_move_fast_02);

		// BOOST UP (fly up)
		setAnimationFrames(PlayerAnimations.BOOST_UP, R.drawable.skin_player_torch_fly_01, R.drawable.skin_player_torch_fly_02, R.drawable.skin_player_torch_fly_03);

		// BOOST MOVE (fly diagonal)
		setAnimationFrames(PlayerAnimations.BOOST_MOVE, R.drawable.skin_player_torch_fly_move_01, R.drawable.skin_player_torch_fly_move_02, R.drawable.skin_player_torch_fly_move_03);

		// BOOST MOVE FAST (fly diagonal fast)
		setAnimationFrames(PlayerAnimations.BOOST_MOVE_FAST, R.drawable.skin_player_torch_fly_move_fast_01, R.drawable.skin_player_torch_fly_move_fast_02, R.drawable.skin_player_torch_fly_move_fast_03);

		// MOVE AIR
		setAnimationFrames(PlayerAnimations.MOVE_AIR, R.drawable.skin_player_torch_move_01, R.drawable.skin_player_torch_move_01);

		// FALL
		setAnimationFrames(PlayerAnimations.FALL, R.drawable.skin_player_torch_air_fall_01, R.drawable.skin_player_torch_air_fall_02);
	}

	private void setActionAnimations() {
		// STOMP
		setAnimationFrames(PlayerAnimations.STOMP, R.drawable.skin_player_torch_stomp_01, R.drawable.skin_player_torch_stomp_02, R.drawable.skin_player_torch_stomp_03);
		// setAnimationFrames(PlayerAnimations.STOMP, R.drawable.skin_player_torch_stomp_2_01, R.drawable.skin_player_torch_stomp_2_02, R.drawable.skin_player_torch_stomp_2_03);

		// HIT REACT
		setAnimationFrames(PlayerAnimations.HIT_REACT, R.drawable.skin_player_torch_hit_01);

		// JET
		setAnimationFrames(PlayerAnimations.JET, R.drawable.skin_player_torch_jet_fire_01, R.drawable.skin_player_torch_jet_fire_02);

		// SHIELD
		setAnimationFrames(PlayerAnimations.SHIELD, R.drawable.skin_player_torch_ghost_01, R.drawable.skin_player_torch_ghost_02, R.drawable.skin_player_torch_ghost_03);
	}

}
