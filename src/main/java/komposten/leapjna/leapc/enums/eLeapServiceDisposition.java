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
 * The service status codes/flags.
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.1.3
 */
public enum eLeapServiceDisposition implements IntEnum
{
	/**
	 * Indicates that an unknown flag, i.e. one which doesn't match any of the
	 * constants, was encountered.
	 */
	Unknown(-1),

	/** No flags. */
	None(0),

	/**
	 * The service cannot receive frames fast enough from the underlying hardware.
	 */
	LowFpsDetected(1),

	/**
	 * The service has paused itself due to an insufficient frame rate from the
	 * hardware.
	 */
	PoorPerformancePause(2),

	/**
	 * The service has failed to start tracking due to unknown reasons.
	 * 
	 * @since Ultraleap Gemini SDK 5.1.16
	 */
	TrackingErrorUnknown(4),

	/** The combination of all valid flags in this enumeration. */
	All(LowFpsDetected.value | PoorPerformancePause.value | TrackingErrorUnknown.value);

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