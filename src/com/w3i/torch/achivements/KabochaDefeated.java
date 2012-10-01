package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class KabochaDefeated extends Achievement {

	public KabochaDefeated() {
		setName(R.string.achievement_kaboocha_name);
		setDescription(R.string.achievement_kaboocha_description);
		setType(Type.KABOCHA_DEFEATED);
		setLocked(true);
		setImageDone(R.drawable.achv_unlocked);
		setImageLocked(R.drawable.achv_locked);
	}
}
