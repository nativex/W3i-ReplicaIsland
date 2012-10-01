package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class KyleDefeatedAchievement extends Achievement {

	public KyleDefeatedAchievement() {
		setName(R.string.achievement_kyle_name);
		setDescription(R.string.achievement_kyle_description);
		setType(Type.KYLE_DEFEATED);
		setLocked(true);
		setImageDone(R.drawable.achv_unlocked);
		setImageLocked(R.drawable.achv_locked);
	}
}
