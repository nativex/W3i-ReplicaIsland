package com.w3i.torch.achivements;

public class GodlikeAchievement extends Achievement {
	private boolean failed = false;

	public GodlikeAchievement() {
		setName(AchievementConstants.GODLIKE_NAME);
		setDescription(AchievementConstants.GODLIKE_DESCRIPTION);
		setType(Type.GODLIKE);
	}

	public void onState(
			State state) {
		switch (state) {
		case START:
			failed = false;
			break;

		case FAIL:
			failed = true;
			break;

		case FINISH:
			setDone(!failed);
			break;
		}

	};
}
