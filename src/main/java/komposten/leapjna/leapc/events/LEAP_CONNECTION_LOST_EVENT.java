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


/**
 * <p>
 * Received from {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}
 * when a connection to the Ultraleap Tracking Service is lost.
 * </p>
 * <p>
 * If a LeapC function that performs a transaction with the Ultraleap Tracking Service is
 * called after the connection is lost, the next call to LeapPollConnection() will return
 * this event. Otherwise, it can take up to 5 seconds of polling the connection to receive
 * this event.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv426LEAP_CONNECTION_LOST_EVENT">LeapC
 *      API - LEAP_CONNECTION_LOST_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "flags" })
public class LEAP_CONNECTION_LOST_EVENT extends Structure implements LEAP_EVENT
{
	/** Reserved for future use. */
	public int flags;


	public LEAP_CONNECTION_LOST_EVENT(Pointer pointer)
	{
		super(pointer);
		read();
	}
}
