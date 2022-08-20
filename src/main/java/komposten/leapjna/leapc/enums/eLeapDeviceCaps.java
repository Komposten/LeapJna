/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
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
 * Flags enumerating Leap device capabilities.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv415eLeapDeviceCaps">LeapC
 *      API - eLeapDeviceCaps</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
public enum eLeapDeviceCaps implements IntFlagEnum<eLeapDeviceCaps>
{
	/** The device has no specific capabilities. */
	None(0x00000000),

	/** The device can send color images. */
	Color(0x00000001);

	public final int value;

	private eLeapDeviceCaps(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}


	@Override
	public eLeapDeviceCaps getEmptyMaskConstant()
	{
		return None;
	}


	public static int createMask(eLeapDeviceCaps... flags)
	{
		return Enums.createMask(flags);
	}
}