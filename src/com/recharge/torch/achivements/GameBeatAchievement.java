package com.recharge.torch.achivements;

import com.recharge.torch.R;


public class GameBeatAchievement extends Achievement {

	public GameBeatAchievement() {
		setName(R.string.achievement_game_beat_name);
		setDescription(R.string.achievement_game_beat_description);
		setType(Type.GAME_BEAT);
		setImageDone(R.drawable.ui_achievement_game_beat);
		setImageLocked(R.drawable.ui_achievement_game_beat_locked);
	}
}
