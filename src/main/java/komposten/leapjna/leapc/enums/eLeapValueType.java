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
