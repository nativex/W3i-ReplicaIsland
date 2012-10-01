package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class MegakillAchievement extends Achievement {
	public static final int MONSTERS_TO_KILL = 4;

	public MegakillAchievement() {
		setName(R.string.achievement_mega_kill_name);
		setDescription(R.string.achievement_mega_kill_description);
		setType(Type.MEGA_KILL);
		setLocked(true);
		setImageDone(R.drawable.achv_unlocked);
		setImageLocked(R.drawable.achv_locked);
	}
}
