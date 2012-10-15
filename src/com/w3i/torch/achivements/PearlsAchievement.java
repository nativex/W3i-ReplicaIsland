package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class PearlsAchievement extends ProgressAchievement {

	public PearlsAchievement() {
		super(R.integer.achievement_pearls_goal);
		setName(R.string.achievement_pearls_name);
		setDescription(R.string.achievement_pearls_description);
		setType(Type.PEARLS);
		setImageDone(R.drawable.ui_achievement_pearls);
		setImageLocked(R.drawable.ui_achievement_pearls_locked);
	}
}
