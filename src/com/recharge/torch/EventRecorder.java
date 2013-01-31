/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.recharge.torch;

import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementManager;
import com.recharge.torch.gamesplatform.TorchCurrencyManager;
import com.recharge.torch.gamesplatform.TorchCurrencyManager.Currencies;
import com.recharge.torch.store.KillingSpreeDetector;
import com.w3i.common.Log;

public class EventRecorder extends BaseObject {
	public final static int COUNTER_ROBOTS_DESTROYED = 0;
	public final static int COUNTER_PEARLS_COLLECTED = 1;
	public final static int COUNTER_PEARLS_TOTAL = 2;
	public final static int COUNTER_SNAILS_DESTROYED = 3;
	public final static int COUNTER_BATS_DESTROYED = 4;
	public final static int COUNTER_KARAGUINS_DESTROYED = 5;
	public static final int COUNTER_ONIONS_DESTROYED = 6;
	public static final int COUNTER_SKELETONS_DESTROYED = 7;
	public static final int COUNTER_EVENT_STING_DESTROYED = 8;
	public static final int COUNTER_TURRET_DESTROYED = 10;
	public static final int COUNTER_SNAIL_BOMBS_DESTROYED = 11;

	private Vector2 mLastDeathPosition = new Vector2();
	private int mLastEnding = -1;
	private int mRobotsDestroyed = 0;
	private int mPearlsCollected = 0;
	private int mPearlsTotal = 0;

	@Override
	public void reset() {
		mRobotsDestroyed = 0;
		mPearlsCollected = 0;
		mPearlsTotal = 0;
	}

	synchronized void setLastDeathPosition(
			Vector2 position) {
		mLastDeathPosition.set(position);
	}

	synchronized Vector2 getLastDeathPosition() {
		return mLastDeathPosition;
	}

	synchronized void setLastEnding(
			int ending) {
		mLastEnding = ending;
	}

	synchronized int getLastEnding() {
		return mLastEnding;
	}

	synchronized void incrementEventCounter(
			int event) {
		switch (event) {
		case COUNTER_ROBOTS_DESTROYED:
			KillingSpreeDetector.recordKill();
			mRobotsDestroyed++;
			Log.i("EventRecorder: Robot Killed");
			break;
		case COUNTER_PEARLS_COLLECTED:
			TorchCurrencyManager.addBalance(Currencies.PEARLS, 1);
			AchievementManager.incrementAchievementProgress(Type.PEARLS, 1);
			mPearlsCollected++;
			break;
		case COUNTER_PEARLS_TOTAL:
			mPearlsTotal++;
			break;

		case COUNTER_SNAILS_DESTROYED:
			// Log.i("EventRecorder: Snail Killed");
			// break;

		case COUNTER_SNAIL_BOMBS_DESTROYED:
			// Log.i("EventRecorder: Snail Bombs Killed");
			// break;
		case COUNTER_BATS_DESTROYED:
			// Log.i("EventRecorder: Bat Killed");
			// break;
		case COUNTER_KARAGUINS_DESTROYED:
			// Log.i("EventRecorder: Karaguin Killed");
			// break;
		case COUNTER_ONIONS_DESTROYED:
			// Log.i("EventRecorder: Onion Killed");
			// break;
		case COUNTER_SKELETONS_DESTROYED:
			// Log.i("EventRecorder: Skeleton Killed");
			// break;
		case COUNTER_EVENT_STING_DESTROYED:
			// Log.i("EventRecorder: Sting Killed");
			// break;
		case COUNTER_TURRET_DESTROYED:
			KillingSpreeDetector.recordKill();
			// Log.i("EventRecorder: Turret Killed");
			break;
		}
	}

	synchronized int getRobotsDestroyed() {
		return mRobotsDestroyed;
	}

	synchronized int getPearlsCollected() {
		return mPearlsCollected;
	}

	synchronized int getPearlsTotal() {
		return mPearlsTotal;
	}
}
