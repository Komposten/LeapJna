/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapConnectionStatus;


/**
 * <p>
 * Information about a connection.
 * </p>
 * <p>
 * Instances of this struct are created by
 * {@link LeapC#LeapGetConnectionInfo(Pointer, LEAP_CONNECTION_INFO)}.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv420LEAP_CONNECTION_INFO">LeapC
 *      API - LEAP_CONNECTION_INFO</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "size", "status" })
public class LEAP_CONNECTION_INFO extends Structure
{
	/** The size of this structure. */
	public int size;

	/**
	 * <p>
	 * The current status of this connection. Use {@link #getStatus()} to get the status as
	 * a {@link eLeapConnectionStatus} value.
	 * </p>
	 * <p>
	 * <b>Note</b>: This appears to <em>always</em> be
	 * {@link eLeapConnectionStatus#NotConnected}.
	 * </p>
	 */
	public long status;

	public LEAP_CONNECTION_INFO()
	{
		super(ALIGN_NONE);
		size = size();
	}


	/**
	 * @return The connection status as an {@link eLeapConnectionStatus} instead of a <code>long</code>.
	 */
	public eLeapConnectionStatus getStatus()
	{
		return Enums.parse((int) status, eLeapConnectionStatus.Unknown);
	}
}