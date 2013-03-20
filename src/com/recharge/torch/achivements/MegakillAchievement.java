package com.recharge.torch.achivements;

import com.recharge.torch.R;

public class MegakillAchievement extends Achievement {
	public static final int MONSTERS_TO_KILL = 4;

	public MegakillAchievement() {
		setName(R.string.achievement_mega_kill_name);
		setDescription(R.string.achievement_mega_kill_description);
		setType(Type.MEGA_KILL);
		setLocked(true);
		setImageDone(R.drawable.ui_achievement_mega_kill);
		setImageLocked(R.drawable.ui_achievement_mega_kill_locked);
		makeDescriptionReplacement(getDescription());
	}

	protected String makeDescriptionReplacement(
			String description) {
		return description.replace("#", Integer.toString(MONSTERS_TO_KILL));
	}
}
