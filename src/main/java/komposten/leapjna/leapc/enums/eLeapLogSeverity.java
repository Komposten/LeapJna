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
 * System message severity types.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga03d3b2203fa8af12c5436b6974a10fbb">LeapC
 *      API - eLeapLogSeverity</a>
 */
public enum eLeapLogSeverity implements IntEnum
{
	/** The message severity is not known or was not specified. */
	Unknown(0),

	/** A message about a fault that could render the software or device non-functional. */
	Critical(1),

	/** A message warning about a condition that could degrade device capabilities. */
	Warning(2),

	/** A system status message. */
	Information(3);

	public final int value;

	private eLeapLogSeverity(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}
