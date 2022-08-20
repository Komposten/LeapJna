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
import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_VARIANT;


/**
 * <p>
 * The result of a configuration change request. Contains a status of true for a
 * successful change.
 * </p>
 * <p>
 * Call {@link LeapC#LeapSaveConfigValue(Pointer, String, LEAP_VARIANT, LongByReference)}
 * to request a service config change. The change is performed asynchronously -- and may
 * fail. {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} returns
 * this event structure when the request has been processed. Use the requestID value to
 * correlate the response to the originating request.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv424LEAP_CONFIG_CHANGE_EVENT">LeapC
 *      API - LEAP_CONFIG_CHANGE_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "requestID", "status" })
public class LEAP_CONFIG_CHANGE_EVENT extends Structure implements LEAP_EVENT
{
	/** An identifier for correlating the request and response. */
	public int requestID;

	/**
	 * The result of the change operation: true on success; false on failure.
	 */
	public boolean status;

	public LEAP_CONFIG_CHANGE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
