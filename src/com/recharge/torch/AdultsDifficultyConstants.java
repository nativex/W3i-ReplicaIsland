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

import com.recharge.torch.powerups.PowerupTypes;

public class AdultsDifficultyConstants extends DifficultyConstants {

	private static final float FUEL_AIR_REFILL_SPEED = 0.15f;
	private static final float FUEL_GROUND_REFILL_SPEED = 2.0f;
	public static final int MAX_PLAYER_LIFE = 2;
	private static final int COINS_PER_POWERUP = 30;

	public static final float GLOW_DURATION = 10.0f;

	// DDA boosts
	private static final int DDA_STAGE_1_ATTEMPTS = 4;
	private static final int DDA_STAGE_2_ATTEMPTS = 8;
	private static final int DDA_STAGE_1_LIFE_BOOST = 1;
	private static final int DDA_STAGE_2_LIFE_BOOST = 2;
	private static final float DDA_STAGE_1_FUEL_AIR_REFILL_SPEED = 0.15f;
	private static final float DDA_STAGE_2_FUEL_AIR_REFILL_SPEED = 0.22f;

	@Override
	public float getFuelAirRefillSpeed() {
		return FUEL_AIR_REFILL_SPEED * (1 + PowerupTypes.JETPACK_RECHARGE_AIR.getValueFloat());
	}

	@Override
	public float getFuelGroundRefillSpeed() {
		return FUEL_GROUND_REFILL_SPEED * (1 + PowerupTypes.JETPACK_RECHARGE_GROUND.getValueFloat());
	}

	@Override
	public int getMaxPlayerLife() {
		return MAX_PLAYER_LIFE + PowerupTypes.LIFE_POINTS.getValueInt();
	}

	@Override
	public int getCoinsPerPowerup() {
		return Math.max(COINS_PER_POWERUP - PowerupTypes.SHIELD_REQUIREMENT_REDUCTION.getValueInt(), 0);
	}

	@Override
	public float getGlowDuration() {
		return GLOW_DURATION + PowerupTypes.SHIELD_DURATION.getValueFloat();
	}

	@Override
	public int getDDAStage1Attempts() {
		return DDA_STAGE_1_ATTEMPTS;
	}

	@Override
	public int getDDAStage2Attempts() {
		return DDA_STAGE_2_ATTEMPTS;
	}

	@Override
	public int getDDAStage1LifeBoost() {
		return DDA_STAGE_1_LIFE_BOOST;
	}

	@Override
	public int getDDAStage2LifeBoost() {
		return DDA_STAGE_2_LIFE_BOOST;
	}

	@Override
	public float getDDAStage1FuelAirRefillSpeed() {
		return DDA_STAGE_1_FUEL_AIR_REFILL_SPEED * (1 + PowerupTypes.JETPACK_RECHARGE_AIR.getValueFloat());
	}

	@Override
	public float getDDAStage2FuelAirRefillSpeed() {
		return DDA_STAGE_2_FUEL_AIR_REFILL_SPEED * (1 + PowerupTypes.JETPACK_RECHARGE_AIR.getValueFloat());
	}

	@Override
	public float getAdditionalFuelAmount() {
		return (1 + PowerupTypes.JETPACK_AIR_TIME.getValueFloat());
	}
}