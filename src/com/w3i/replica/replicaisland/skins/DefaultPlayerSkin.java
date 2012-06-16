package com.w3i.replica.replicaisland.skins;

import com.w3i.replica.replicaisland.AnimationComponent.PlayerAnimations;
import com.w3i.replica.replicaisland.AnimationFrame;
import com.w3i.replica.replicaisland.CollisionVolume;
import com.w3i.replica.replicaisland.FixedSizeArray;
import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.SpriteAnimation;
import com.w3i.replica.replicaisland.TextureLibrary;
import com.w3i.replica.replicaisland.Utils;

public class DefaultPlayerSkin extends Skin {

	public DefaultPlayerSkin() {
		super();
		init();
	}

	private void init() {
		// idle
		setAnimationFrames(PlayerAnimations.IDLE, R.drawable.andou_stand);

		// move
		setAnimationFrames(PlayerAnimations.MOVE, R.drawable.andou_diag01, R.drawable.andou_diag02, R.drawable.andou_diag03);

		// move fast
		setAnimationFrames(PlayerAnimations.MOVE_FAST, R.drawable.andou_diagmore01, R.drawable.andou_diagmore02, R.drawable.andou_diagmore03);

		// boost up
		setAnimationFrames(PlayerAnimations.BOOST_UP, R.drawable.andou_flyup01, R.drawable.andou_flyup02, R.drawable.andou_flyup03);

		// boost move
		setAnimationFrames(PlayerAnimations.BOOST_MOVE, R.drawable.andou_diag02, R.drawable.andou_diag03);

		// boost move fast
		setAnimationFrames(PlayerAnimations.BOOST_MOVE_FAST, R.drawable.andou_diagmore01, R.drawable.andou_diagmore02, R.drawable.andou_diagmore03);

		// move air
		setAnimationFrames(PlayerAnimations.MOVE_AIR, R.drawable.andou_diag01, R.drawable.andou_diag02, R.drawable.andou_diag03);

		// stomp
		setAnimationFrames(PlayerAnimations.STOMP, R.drawable.andou_stomp01, R.drawable.andou_stomp02, R.drawable.andou_stomp03, R.drawable.andou_stomp04);

		// hit react
		setAnimationFrames(PlayerAnimations.HIT_REACT, R.drawable.andou_hit);

		// death
		int[] deathAnimationFrames = new int[16];
		deathAnimationFrames[0] = R.drawable.andou_die01;
		deathAnimationFrames[1] = R.drawable.andou_die02;
		deathAnimationFrames[2] = R.drawable.andou_die01;
		deathAnimationFrames[3] = R.drawable.andou_die02;
		deathAnimationFrames[4] = R.drawable.andou_explode01;
		deathAnimationFrames[5] = R.drawable.andou_explode02;
		deathAnimationFrames[6] = R.drawable.andou_explode03;
		deathAnimationFrames[7] = R.drawable.andou_explode04;
		deathAnimationFrames[8] = R.drawable.andou_explode05;
		deathAnimationFrames[9] = R.drawable.andou_explode06;
		deathAnimationFrames[10] = R.drawable.andou_explode07;
		deathAnimationFrames[11] = R.drawable.andou_explode08;
		deathAnimationFrames[12] = R.drawable.andou_explode09;
		deathAnimationFrames[13] = R.drawable.andou_explode10;
		deathAnimationFrames[14] = R.drawable.andou_explode11;
		deathAnimationFrames[15] = R.drawable.andou_explode12;
		setAnimationFrames(PlayerAnimations.DEATH, deathAnimationFrames);

		// frozen
		setAnimationFrames(PlayerAnimations.FROZEN, new int[0]);

		setSkinType(SkinType.PLAYER);
		setImage(R.drawable.andou_stand);
		setName("Android");
		setDescription("An android create by the Kaboocha to search for The Source");
	}

	@Override
	public SpriteAnimation getSpriteAnimation(
			PlayerAnimations animationType,
			float animationHoldTime,
			FixedSizeArray<CollisionVolume> pressAndCollectVolume,
			FixedSizeArray<CollisionVolume> vulnerabilityVolume,
			boolean isLooping) {
		return getSpriteAnimation(animationType, animationHoldTime, pressAndCollectVolume, vulnerabilityVolume, isLooping, true);
	}

	@Override
	public SpriteAnimation getSpriteAnimation(
			PlayerAnimations animationType,
			float animationHoldTime,
			FixedSizeArray<CollisionVolume> pressAndCollectVolume,
			FixedSizeArray<CollisionVolume> vulnerabilityVolume,
			boolean isLooping,
			boolean loadDefaultOnFail) {
		if (animationType == PlayerAnimations.DEATH) {
			TextureLibrary textureLibrary = getTextureLibrary();
			if (textureLibrary == null) {
				throw new NullPointerException("TextureLibrary is null.");
			}
			int[] animationFrames = getAnimationFrames(PlayerAnimations.DEATH);
			SpriteAnimation deathAnim = new SpriteAnimation(PlayerAnimations.DEATH.ordinal(), 16);
			AnimationFrame death1 = new AnimationFrame(textureLibrary.allocateTexture(animationFrames[0]), Utils.framesToTime(24, 1), null, null);
			AnimationFrame death2 = new AnimationFrame(textureLibrary.allocateTexture(animationFrames[1]), Utils.framesToTime(24, 1), null, null);
			deathAnim.addFrame(death1);
			deathAnim.addFrame(death2);
			deathAnim.addFrame(death1);
			deathAnim.addFrame(death2);
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[4]), Utils.framesToTime(24, 1), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[5]), Utils.framesToTime(24, 1), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[6]), Utils.framesToTime(24, 1), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[7]), Utils.framesToTime(24, 1), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[8]), Utils.framesToTime(24, 2), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[9]), Utils.framesToTime(24, 2), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[10]), Utils.framesToTime(24, 2), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[11]), Utils.framesToTime(24, 2), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[12]), Utils.framesToTime(24, 2), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[13]), Utils.framesToTime(24, 2), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[14]), Utils.framesToTime(24, 2), null, null));
			deathAnim.addFrame(new AnimationFrame(textureLibrary.allocateTexture(animationFrames[15]), Utils.framesToTime(24, 2), null, null));
			return deathAnim;
		}
		return super.getSpriteAnimation(animationType, animationHoldTime, pressAndCollectVolume, vulnerabilityVolume, isLooping, loadDefaultOnFail);
	}

}
