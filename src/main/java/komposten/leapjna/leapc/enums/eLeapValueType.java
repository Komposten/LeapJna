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

import komposten.leapjna.leapc.data.LEAP_VARIANT;
import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * <p>
 * Different value types.
 * </p>
 * <p>
 * Used by {@link LEAP_VARIANT} to keep track of the type of value the variant
 * stores.
 * </p>
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv414eLeapValueType">LeapC
 *      API - eLeapValueType</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
public enum eLeapValueType implements IntEnum
{
	/** The type is unknown (which is an abnormal condition). */
	Unknown(0),
	Boolean(1),
	Int32(2),
	Float(3),
	String(4);

	public final int value;

	private eLeapValueType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}
