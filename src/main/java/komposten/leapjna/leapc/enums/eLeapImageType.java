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

import komposten.leapjna.leapc.enums.Enums.IntEnum;

/**
 * Functional image types (not data formats).
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv414eLeapImageType">LeapC
 *      API - eLeapImageType</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
public enum eLeapImageType implements IntEnum
{
	/** An invalid or unknown type. */
	Unknown(0),

	/** Default, processed IR images. */
	Default(1),

	/** Raw images from the device. */
	Raw(2);

	public final int value;

	private eLeapImageType(int value)
	{
		this.value = value;
	}
	
	
	@Override
	public int getValue()
	{
		return value;
	}
}
