package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class HitAchievement extends ProgressAchievement {

	public HitAchievement() {
		super(R.integer.achievement_hit_goal);
		setName(R.string.achievement_hit_name);
		setDescription(R.string.achievement_hit_description);
		setType(Type.HIT);
		setImageLocked(R.drawable.ui_achievement_hit_locked);
		setImageDone(R.drawable.ui_achievement_hit);
	}

}
