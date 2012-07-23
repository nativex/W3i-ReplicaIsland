package com.w3i.torch.achivements;

public class AchievementData<T> {
	private T data;

	public AchievementData() {

	}

	public AchievementData(T data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(
			T data) {
		this.data = data;
	}

}
