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

import komposten.leapjna.leapc.data.LEAP_DEVICE_REF;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;


/**
 * <p>
 * A notification that a device's status has changed.
 * </p>
 * <p>
 * One of these messages is received by the client as soon as the service is connected, or
 * when a new device is attached.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv431LEAP_DEVICE_STATUS_CHANGE_EVENT">LeapC
 *      API - LEAP_DEVICE_STATUS_CHANGE_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.1.3
 */
@FieldOrder({ "device", "last_status", "status" })
public class LEAP_DEVICE_STATUS_CHANGE_EVENT extends Structure implements LEAP_EVENT
{
	/** The handle reference of the newly attached device. */
	public LEAP_DEVICE_REF device;

	/**
	 * <p>
	 * The last known status of the device.
	 * </p>
	 * <p>
	 * A combination of flags from the {@link eLeapDeviceStatus} collection.
	 * </p>
	 * <p>
	 * Use {@link #getLastStatus()} to get the status as a {@link eLeapDeviceStatus} value.
	 * </p>
	 */
	public int last_status;

	/**
	 * <p>
	 * The current status of the device.
	 * </p>
	 * <p>
	 * A combination of flags from the {@link eLeapDeviceStatus} collection.
	 * </p>
	 * <p>
	 * Use {@link #getStatus()} to get the status as a {@link eLeapDeviceStatus} value.
	 * </p>
	 */
	public int status;

	public LEAP_DEVICE_STATUS_CHANGE_EVENT(Pointer pointer)
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


	/**
	 * @return The last status flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code> mask.
	 */
	public eLeapDeviceStatus[] getLastStatus()
	{
		return Enums.parseMask(last_status, eLeapDeviceStatus.class);
	}
}
