/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.data.LEAP_HAND;
import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * <p>
 * The hand chirality types.
 * </p>
 * <p>
 * Used in the {@link LEAP_HAND} struct.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga6d751aedb178355c21ec1cac4706e044">LeapC
 *      API - eLeapHandType</a>
 */
public enum eLeapHandType implements IntEnum
{
	Left(0x000),
	Right(0x001),
	Unknown(-0x001);

	public final int value;

	private eLeapHandType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}