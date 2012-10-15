package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class GoodEndingAchievement extends Achievement {

	public GoodEndingAchievement() {
		setType(Type.GOOD_ENDING);
		setName(R.string.achievement_good_boy_name);
		setDescription(R.string.achievement_good_boy_description);
		setLocked(true);
		setImageDone(R.drawable.ui_achievement_good_boy);
		setImageLocked(R.drawable.ui_achievement_good_boy_locked);
	}
}
