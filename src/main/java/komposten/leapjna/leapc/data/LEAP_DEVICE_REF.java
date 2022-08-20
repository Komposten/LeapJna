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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.IntByReference;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.util.ArrayPointer;


/**
 * <p>
 * A reference to a Leap device.
 * </p>
 * <p>
 * Get a <code>LEAP_DEVICE_REF</code> by calling
 * {@link LeapC#LeapGetDeviceList(Pointer, ArrayPointer, IntByReference)}. Access a
 * device by calling {@link LeapC#LeapOpenDevice(LEAP_DEVICE_REF, LEAP_DEVICE)} with this
 * reference. <code>LeapOpenDevice()</code> provides a {@link LEAP_DEVICE} struct, which
 * is a handle to an open device.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv415LEAP_DEVICE_REF">LeapC
 *      API - LEAP_DEVICE_REF</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "handle", "id" })
public class LEAP_DEVICE_REF extends Structure implements Structure.ByValue
{
	/** A device handle. */
	public Pointer handle;

	/** A generic identifier. */
	public int id;

	public LEAP_DEVICE_REF()
	{
		super(ALIGN_NONE);
	}

	public LEAP_DEVICE_REF(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
