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
 * System message severity types.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv416eLeapLogSeverity">LeapC
 *      API - eLeapLogSeverity</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
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
