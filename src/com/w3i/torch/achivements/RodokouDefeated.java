package com.w3i.torch.achivements;

import com.w3i.torch.R;

public class RodokouDefeated extends Achievement {

	public RodokouDefeated() {
		setName(R.string.achievement_rodokuo_name);
		setDescription(R.string.achievement_rodokuo_description);
		setType(Type.RODOKOU_DEFEATED);
		setLocked(true);
		setImageDone(R.drawable.achv_unlocked);
		setImageLocked(R.drawable.achv_locked);
	}
}
