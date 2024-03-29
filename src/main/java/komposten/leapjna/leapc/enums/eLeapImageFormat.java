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

import komposten.leapjna.leapc.enums.Enums.IntEnum;

/**
 * Image formats.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv416eLeapImageFormat">LeapC
 *      API - eLeapImageFormat</a>
 */
public enum eLeapImageFormat implements IntEnum
{
	/** An invalid or unknown format. */
	Unknown(0),

	/** An infrared image. */
	IR(0x317249),

	/** A Bayer RGBIr image with uncorrected RGB channels. */
	RGBIr(0x49425247);

	public final int value;

	private eLeapImageFormat(int value)
	{
		this.value = value;
	}
	
	
	@Override
	public int getValue()
	{
		return value;
	}
}
