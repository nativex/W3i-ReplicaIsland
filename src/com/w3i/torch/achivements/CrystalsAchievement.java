package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class CrystalsAchievement extends ProgressAchievement {

	public CrystalsAchievement() {
		super(R.integer.achievement_crystals_goal);
		setName(R.string.achievement_crystals_name);
		setDescription(R.string.achievement_crystals_description);
		setType(Type.CRYSTALS);
		setImageDone(R.drawable.ui_achievement_crystal_collector);
		setImageLocked(R.drawable.ui_achievement_crystal_collector_locked);
	}
}
