package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class MultikillAchievement extends ProgressAchievement {

	public MultikillAchievement() {
		super(R.integer.achievement_multi_kill_goal);
		setName(R.string.achievement_multi_kill_name);
		setDescription(R.string.achievement_multi_kill_description);
		setType(Type.MULTI_KILL);
		setLocked(true);
		setImageDone(R.drawable.achv_unlocked);
		setImageLocked(R.drawable.achv_locked);
	}

}
