package com.recharge.torch.skins;

import java.util.HashMap;

import com.recharge.torch.AnimationComponent;
import com.recharge.torch.AnimationComponent.PlayerAnimations;
import com.recharge.torch.AnimationFrame;
import com.recharge.torch.CollisionVolume;
import com.recharge.torch.FixedSizeArray;
import com.recharge.torch.SpriteAnimation;
import com.recharge.torch.TextureLibrary;

public class Skin {
	private HashMap<PlayerAnimations, int[]> animationFramesContainer;
	private TextureLibrary texLib;
	private DefaultPlayerSkin defaultSkin;
	private int image;
	private int titleImage;
	private String name;
	private String description;
	private SkinType skinType = SkinType.UNDEFINED;

	protected enum SkinType {
		PLAYER, ENEMY, NPC, PROJECTILE, UNDEFINED
	}

	protected Skin() {
		animationFramesContainer = new HashMap<AnimationComponent.PlayerAnimations, int[]>();
	}

	public SpriteAnimation getSpriteAnimation(
			PlayerAnimations animationType,
			float animationHoldTime,
			FixedSizeArray<CollisionVolume> pressAndCollectVolume,
			FixedSizeArray<CollisionVolume> vulnerabilityVolume,
			boolean isLooping) {
		return getSpriteAnimation(animationType, animationHoldTime, pressAndCollectVolume, vulnerabilityVolume, isLooping, true);
	}

	public int[] getAnimationFrames(
			PlayerAnimations animationType) {
		return animationFramesContainer.get(animationType);
	}

	protected TextureLibrary getTextureLibrary() {
		return texLib;
	}

	public void setTextureLibrary(
			TextureLibrary textureLibrary) {
		texLib = textureLibrary;
	}

	public SpriteAnimation getSpriteAnimation(
			PlayerAnimations animationType,
			float animationHoldTime,
			FixedSizeArray<CollisionVolume> pressAndCollectVolume,
			FixedSizeArray<CollisionVolume> vulnerabilityVolume,
			boolean isLooping,
			boolean loadDefaultOnFail) {
		if (texLib == null) {
			throw new NullPointerException("TextureLibrary is null.");
		}
		int[] animationFrames = animationFramesContainer.get(animationType);
		SpriteAnimation animation = null;

		if (animationFrames == null) {
			if (loadDefaultOnFail) {
				if (defaultSkin == null) {
					if (!setDefaultSkin()) {
						return null;
					}
				}
				animation = defaultSkin.getSpriteAnimation(animationType, animationHoldTime, pressAndCollectVolume, vulnerabilityVolume, isLooping, false);
			}
		} else if (animationFrames.length > 0) {
			animation = new SpriteAnimation(animationType.ordinal(), animationFrames.length);
			for (int i = 0; i < animationFrames.length; i++) {
				animation.addFrame(new AnimationFrame(texLib.allocateTexture(animationFrames[i]), animationHoldTime, pressAndCollectVolume, vulnerabilityVolume));
			}
		}
		if ((isLooping) && (animation != null)) {
			animation.setLoop(true);
		}
		return animation;
	}

	private boolean setDefaultSkin() {
		switch (skinType) {
		case ENEMY:
			defaultSkin = null;
		case NPC:
			defaultSkin = null;
		case PLAYER:
			defaultSkin = new DefaultPlayerSkin();
			defaultSkin.setTextureLibrary(texLib);
			break;
		case PROJECTILE:
			defaultSkin = null;
		case UNDEFINED:
			throw new NullPointerException("Skin doesn't have type (" + getClass().getSimpleName() + ")");
		}
		return defaultSkin != null;
	}

	protected void setAnimationFrames(
			PlayerAnimations animationType,
			int... frames) {
		if ((frames == null) || (frames.length == 0)) {
			frames = new int[0];
		}
		animationFramesContainer.put(animationType, frames);
	}

	protected void setSkinType(
			SkinType type) {
		skinType = type;
	}

	/**
	 * @return the image
	 */
	public int getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	protected void setImage(
			int image) {
		this.image = image;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	protected void setName(
			String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	protected void setDescription(
			String description) {
		this.description = description;
	}

	/**
	 * @return the titleImage
	 */
	public int getTitleImage() {
		return titleImage;
	}

	/**
	 * @param titleImage
	 *            the titleImage to set
	 */
	public void setTitleImage(
			int titleImage) {
		this.titleImage = titleImage;
	}

}
