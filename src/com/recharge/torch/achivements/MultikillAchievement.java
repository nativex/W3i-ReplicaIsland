package com.recharge.torch.achivements;

import com.recharge.torch.R;


public class MultikillAchievement extends ProgressAchievement {

	public MultikillAchievement() {
		super(R.integer.achievement_multi_kill_goal);
		setName(R.string.achievement_multi_kill_name);
		setDescription(R.string.achievement_multi_kill_description);
		setType(Type.MULTI_KILL);
		setLocked(true);
		setImageDone(R.drawable.ui_achievement_multikill);
		setImageLocked(R.drawable.ui_achievement_multikill_locked);
	}

}
