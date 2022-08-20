/*
 * Copyright 2021-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntEnum;
import komposten.leapjna.leapc.events.LEAP_TRACKING_MODE_EVENT;


/**
 * <p>
 * Enumerates values for the tracking mode.
 * </p>
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv417eLeapTrackingMode">LeapC
 *      API - eLeapTrackingMode</a>
 * @since LeapJna 1.1.0
 * @since Ultraleap Gemini SDK 5.0.0
 */
public enum eLeapTrackingMode implements IntEnum
{
	/** The tracking mode optimised for desktop devices */
	Desktop(0),

	/** The tracking mode optimised for head-mounted devices */
	HMD(1),

	/** The tracking mode optimised for screen top-mounted devices */
	ScreenTop(2),

	/** Tracking mode is not known (allows triggering of a new {@link LEAP_TRACKING_MODE_EVENT}) */
	Unknown(3);

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