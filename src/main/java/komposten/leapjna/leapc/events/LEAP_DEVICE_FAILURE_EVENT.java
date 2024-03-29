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
import komposten.leapjna.leapc.data.LEAP_DEVICE;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;


/**
 * <p>
 * Device event information.
 * </p>
 * <p>
 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} produces a
 * message containing this event when a new device is detected. You can use the handle
 * provided by the device filed to open a device so that you can access its properties.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv425LEAP_DEVICE_FAILURE_EVENT">LeapC
 *      API - LEAP_DEVICE_FAILURE_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "status", "hDevice" })
public class LEAP_DEVICE_FAILURE_EVENT extends Structure implements LEAP_EVENT
{
	/** The status of this failure event. */
	public int status;

	/**
	 * <p>
	 * A handle to the device generating this failure event, if available, otherwise
	 * <code>null</code>.
	 * </p>
	 * <p>
	 * You are not responsible for closing this handle.
	 * </p>
	 */
	public LEAP_DEVICE hDevice;

	public LEAP_DEVICE_FAILURE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The status flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code> mask.
	 */
	public eLeapDeviceStatus[] getStatus()
	{
		return Enums.parseMask(status, eLeapDeviceStatus.class);
	}

}
