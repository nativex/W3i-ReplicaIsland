package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class KabochaDefeated extends Achievement {

	public KabochaDefeated() {
		setName(R.string.achievement_kaboocha_name);
		setDescription(R.string.achievement_kaboocha_description);
		setType(Type.KABOCHA_DEFEATED);
		setLocked(true);
		setImageDone(R.drawable.ui_achievement_kaboocha);
		setImageLocked(R.drawable.ui_achievement_kaboocha_locked);
	}
}
