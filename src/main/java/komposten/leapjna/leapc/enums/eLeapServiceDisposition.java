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

public enum eLeapServiceDisposition implements IntEnum
{
	/** No flags. */
	None(0),
	
	/** The service cannot receive frames fast enough from the underlying hardware. */
	LowFpsDetected(1),

	/**
	 * The service has paused itself due to an insufficient frame rate from the hardware.
	 */
	PoorPerformancePause(2),

	/** The combination of all valid flags in this enumeration. */
	All(1 | 2);

	public final int value;

	private eLeapServiceDisposition(int value)
	{
		this.value = value;
	}
	
	
	@Override
	public int getValue()
	{
		return value;
	}
}