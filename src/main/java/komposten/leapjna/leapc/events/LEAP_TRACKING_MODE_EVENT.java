/*
 * Copyright 2021-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapTrackingMode;


/**
 * <p>
 * The response from a request to get or set a policy.
 * </p>
 * <p>
 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} creates this
 * struct when the response becomes available.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv424LEAP_TRACKING_MODE_EVENT">LeapC
 *      API - LEAP_TRACKING_MODE_EVENT</a>
 * @since LeapJna 1.1.0
 * @since Ultraleap Gemini SDK 5.0.0
 */
@FieldOrder({ "reserved", "current_tracking_mode" })
public class LEAP_TRACKING_MODE_EVENT extends Structure implements LEAP_EVENT
{
	/** Reserved for future use. */
	public int reserved;

	/**
	 * An enum specifying the tracking mode effective at the time the tracking mode event
	 * was processed. Use {@link #getCurrentTrackingMode()} to get the tracking mode as an
	 * {@link eLeapTrackingMode} value.
	 */
	public int current_tracking_mode;

	public LEAP_TRACKING_MODE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The tracking mode as an {@link eLeapTrackingMode} instead of an <code>int</code>.
	 */
	public eLeapTrackingMode getCurrentTrackingMode()
	{
		return Enums.parse(current_tracking_mode, eLeapTrackingMode.Unknown);
	}
}
