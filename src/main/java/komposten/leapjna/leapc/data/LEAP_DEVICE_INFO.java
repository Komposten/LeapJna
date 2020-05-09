/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDeviceCaps;
import komposten.leapjna.leapc.enums.eLeapDevicePID;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;


/**
 * <p>
 * Properties of a Leap device.
 * </p>
 * <p>
 * Get a <code>LEAP_DEVICE_INFO</code> by calling
 * {@link LeapC#LeapGetDeviceInfo(Pointer, LEAP_DEVICE_INFO)} with the handle for device.
 * The device must be open.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___d_e_v_i_c_e___i_n_f_o">LeapC
 *      API - LEAP_DEVICE_INFO</a>
 */
@FieldOrder({ "size", "status", "caps", "pid", "baseline", "serial_length", "serial",
		"h_fov", "v_fov", "range" })
public class LEAP_DEVICE_INFO extends Structure
{
	/** Size of this structure. */
	public int size;

	/**
	 * A combination of {@link eLeapDeviceStatus} flags. Use {@link #getStatus()} to get the
	 * status flags as an array of {@link eLeapDeviceStatus} values.
	 */
	public int status;

	/**
	 * A combination of {@link eLeapDeviceCaps} flags. Use {@link #getCapabilities()} to get
	 * the capabilities as an array of {@link eLeapDeviceCaps} values.
	 */
	public int caps;

	/**
	 * One of the {@link eLeapDevicePID} members. Use {@link #getPid()} to get the product
	 * ID as an {@link eLeapDevicePID} value.
	 */
	public int pid;

	/**
	 * The device baseline, in micrometers.
	 *
	 * The baseline is defined as the distance between the center axis of each lens in a
	 * stereo camera system. For other camera systems, this value is set to zero.
	 */
	public int baseline;

	/**
	 * The required length of the serial number char buffer including the null character.
	 */
	public int serial_length;

	/** A pointer to the null-terminated device serial number string. */
	public String serial;

	/** The horizontal field of view of this device in radians. */
	public float h_fov;

	/** The vertical field of view of this device in radians. */
	public float v_fov;

	/** The maximum range for this device, in micrometers. */
	public int range;

	public LEAP_DEVICE_INFO()
	{
		this(1);
	}


	public LEAP_DEVICE_INFO(int serial_length)
	{
		super(ALIGN_NONE);

		size = size();
		allocateSerialBuffer(serial_length);
	}


	/**
	 * Allocates space for the serial number string.
	 * 
	 * @param serial_length The length of the serial string.
	 */
	public void allocateSerialBuffer(int serial_length)
	{
		setSerial(new String(new byte[serial_length]));
	}


	/**
	 * Sets the serial string to the provided value, updates {@link #serial_length}, and
	 * writes to native memory.
	 * 
	 * @param serial The value to assign to the serial string.
	 */
	private void setSerial(String serial)
	{
		this.serial_length = serial.getBytes().length;
		this.serial = serial;
		write();
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
	 * @return The device capabilities as an {@link eLeapDeviceCaps} array instead of an
	 *         <code>int</code> mask.
	 */
	public eLeapDeviceCaps[] getCapabilities()
	{
		return Enums.parseMask(caps, eLeapDeviceCaps.class);
	}


	/**
	 * @return The device product ID as an {@link eLeapDevicePID} instead of an
	 *         <code>int</code>.
	 */
	public eLeapDevicePID getPid()
	{
		return Enums.parse(pid, eLeapDevicePID.Invalid);
	}
}
