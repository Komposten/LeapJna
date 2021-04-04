/*
 * Copyright 2021 Jakob Hjelm (Komposten)
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
 * <p>
 * Enumerates values for the tracking mode.
 * </p>
 * @since 1.1.0 (Gemini 5.0.0)
 */
public enum eLeapTrackingMode implements IntEnum
{
	Unknown(-1),
	
	/** The tracking mode optimised for desktop devices */
	Desktop(0),

	/** The tracking mode optimised for head-mounted devices */
	HMD(1),

	/** The tracking mode optimised for screen top-mounted devices */
	ScreenTop(2);

	public final int value;

	private eLeapTrackingMode(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}