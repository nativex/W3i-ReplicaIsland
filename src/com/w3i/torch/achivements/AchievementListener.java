package com.w3i.torch.achivements;

public interface AchievementListener {

	public void achievementDone(
			Achievement achievement);

	public void achievementUnlocked(
			Achievement achievement);

	public void achievementProgressUpdate(
			Achievement achievement,
			int percentDone);
}