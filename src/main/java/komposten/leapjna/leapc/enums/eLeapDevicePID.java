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

import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * Device hardware types.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga5ed369865afe29bdadc65c450eb44c75">LeapC
 *      API - eLeapDevicePID</a>
 */
public enum eLeapDevicePID implements IntEnum
{
	/** An unknown device. */
	Unknown(0x0000),

	/** The Leap Motion consumer peripheral. */
	Peripheral(0x0003),

	/** Internal research product codename "Dragonfly". */
	Dragonfly(0x1102),

	/** Internal research product codename "Nightcrawler". */
	Nightcrawler(0x1201),

	/** Research product codename "Rigel". */
	Rigel(0x1202),

	/** An invalid device type value. */
	Invalid(0xFFFFFFFF);

	public final int value;

	private eLeapDevicePID(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}