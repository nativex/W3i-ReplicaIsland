package com.recharge.torch.utils;

public class TimeUtils {

	public static String convertSecondsToString(
			long secondsTotal) {
		long seconds = secondsTotal % 60;
		secondsTotal /= 60;
		long minutes = secondsTotal % 60;
		long hours = secondsTotal / 60;

		String time = (hours <= 0) ? "" : Long.toString(hours) + ":";
		time += (minutes < 10 ? "0" + minutes : Long.toString(minutes)) + ":";
		time += (seconds < 10 ? "0" + seconds : Long.toString(seconds));
		return time;
	}

	public static String convertMilisecondsToString(
			long miliseconds) {
		return convertSecondsToString(miliseconds / 1000);
	}

	public static long getSecondsFromMilliseconds(
			long miliseconds) {
		return ((miliseconds / 1000) % 60);
	}

	public static long getMinutesFromMilliseconds(
			long milliseconds) {
		return (milliseconds / 60000);
	}

	public static long getHoursFromMilliseconds(
			long milliseconds) {
		return (milliseconds / 3600000);
	}

	public static long getSecondsFromSeconds(
			long seconds) {
		return (seconds % 60);
	}

	public static long getMinutesFromSeconds(
			long seconds) {
		return seconds / 60;
	}

	public static long getHoursFromSeconds(
			long seconds) {
		return seconds / 3600;
	}

	public static String getTimeAchievementStringFromSeconds(
			long secondsTotal) {
		long seconds = secondsTotal % 60;
		secondsTotal /= 60;
		long minutes = secondsTotal % 60;
		long hours = secondsTotal / 60;

		String time = "";
		if (hours > 0) {
			time += Long.toString(hours) + (hours == 1 ? " hour" : " hours");
		}
		if (minutes > 0) {
			if (hours > 0) {
				time += " ";
				if (seconds <= 0) {
					time += " and ";
				}
			}
			time += Long.toString(minutes) + (minutes == 1 ? " minute" : " minutes");
		}
		if (seconds > 0) {
			if ((hours > 0) || (minutes > 0)) {
				time += " and ";
			}
			time += Long.toString(seconds) + (seconds == 1 ? " second" : " seconds");
		}

		return time;
	}
}
