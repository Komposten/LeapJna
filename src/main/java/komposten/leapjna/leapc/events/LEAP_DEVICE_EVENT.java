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
import komposten.leapjna.leapc.data.LEAP_DEVICE_REF;
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
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv417LEAP_DEVICE_EVENT">LeapC
 *      API - LEAP_DEVICE_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "flags", "device", "status" })
public class LEAP_DEVICE_EVENT extends Structure implements LEAP_EVENT
{
	/** Reserved for future use. */
	public int flags;

	/** The handle reference of the newly attached device. */
	public LEAP_DEVICE_REF device;

	/**
	 * <p>
	 * The status of the connected device.
	 * </p>
	 * <p>
	 * A combination of flags from the {@link eLeapDeviceStatus} collection.
	 * </p>
	 * <p>
	 * Use {@link #getStatus()} to get the status as a {@link eLeapDeviceStatus} value.
	 * </p>
	 */
	public int status;

	public LEAP_DEVICE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The status flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code>.
	 */
	public eLeapDeviceStatus[] getStatus()
	{
		return Enums.parseMask(status, eLeapDeviceStatus.class);
	}
}
