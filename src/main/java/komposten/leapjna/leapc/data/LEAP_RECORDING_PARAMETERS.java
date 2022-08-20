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

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;


/**
 * <p>
 * Read/Write mode for opening a {@link LEAP_RECORDING}.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv425LEAP_RECORDING_PARAMETERS">LeapC
 *      API - LEAP_RECORDING_PARAMETERS</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.2.0
 */
@FieldOrder({ "mode" })
public class LEAP_RECORDING_PARAMETERS extends Structure implements Structure.ByValue
{
	/**
	 * <p>
	 * A combination of eLeapRecordingFlags indicating the desired operations.
	 * </p>
	 * <p>
	 * Use {@link #getMode()} to get the mode as an array of {@link eLeapRecordingFlags}
	 * values.
	 * </p>
	 */
	public int mode;

	public LEAP_RECORDING_PARAMETERS()
	{}


	public LEAP_RECORDING_PARAMETERS(eLeapRecordingFlags... flags)
	{
		mode = eLeapRecordingFlags.createMask(flags);
		write();
	}


	/**
	 * @return The mode flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code>.
	 */
	public eLeapRecordingFlags[] getMode()
	{
		return Enums.parseMask(mode, eLeapRecordingFlags.class);
	}
}
