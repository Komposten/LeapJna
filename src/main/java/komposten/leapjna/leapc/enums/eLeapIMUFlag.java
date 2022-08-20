/*
 * Copyright 2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntFlagEnum;


/**
 * @since LeapJna 1.2.0
 * @since Ultraleap Gemini SDK 5.6.0
 */
public enum eLeapIMUFlag implements IntFlagEnum<eLeapIMUFlag>
{
	/** No active flags */
	None(0),

	/** Has accelerometer measurements. */
	HasAccelerometer(1),

	/** Has gyroscope measurements. */
	HasGyroscope(2),

	/** Has a temperature measurement. */
	HasTemperature(4);

	public final int value;

	private eLeapIMUFlag(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}


	@Override
	public eLeapIMUFlag getEmptyMaskConstant()
	{
		return None;
	}


	public static int createMask(eLeapIMUFlag... flags)
	{
		return Enums.createMask(flags);
	}
}