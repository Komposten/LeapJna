/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
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
import komposten.leapjna.leapc.enums.eLeapServiceDisposition;


/**
 * Received from {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}
 * when a connection to the Ultraleap Tracking Service is established.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv421LEAP_CONNECTION_EVENT">LeapC
 *      API - LEAP_CONNECTION_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "flags" })
public class LEAP_CONNECTION_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * A combination of {@link eLeapServiceDisposition} flags. Use {@link #getFlags()} to
	 * get the type as a {@link eLeapServiceDisposition} value.
	 * 
	 * @since Ultraleap Orion SDK 3.1.3
	 */
	public int flags;

	public LEAP_CONNECTION_EVENT(Pointer pointer)
	{
		super(pointer);
		read();
	}


	/**
	 * @return The flags as an {@link eLeapServiceDisposition} instead of an
	 *         <code>int</code>.
	 */
	public eLeapServiceDisposition getFlags()
	{
		return Enums.parse(flags, eLeapServiceDisposition.Unknown);
	}
}
